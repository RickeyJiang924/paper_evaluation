window.onload=function (ev) {
    evolution();
}

function evolution() {
    var evolutionVue=new Vue({
        el:"#evolutionVue",
        data:{
            evolutionRelationChart:"",
            nowTopicId:1,
            nowTopicWeight:0.021574,
            nowTopicYear:"2010.122928(4.438358)",
            allTopicWords:["0.897643:软件工程","0.897643:软件工程","0.897643:软件工程","软件工程","软件工程","软件工程",
                "软件工程","软件工程","软件工程","软件工程","软件工程","软件工程","软件工程",
                "软件工程","软件工程","软件工程","软件工程","软件工程","软件工程","软件工程"],
            allPaperTitle:[
                "0.021514:关于如何创新和完善计算机软件工程管理的探讨",
                "0.021514:关于如何创新和完善计算机软件工程管理的探讨",
                "0.021514:关于如何创新和完善计算机软件工程管理的探讨",
                "0.021514:关于如何创新和完善计算机软件工程管理的探讨",
                "0.021514:关于如何创新和完善计算机软件工程管理的探讨",
                "0.021514:关于如何创新和完善计算机软件工程管理的探讨",
                "0.021514:关于如何创新和完善计算机软件工程管理的探讨",
                "0.021514:关于如何创新和完善计算机软件工程管理的探讨",
                "0.021514:关于如何创新和完善计算机软件工程管理的探讨",
                "0.021514:关于如何创新和完善计算机软件工程管理的探讨",
            ]

        },
        methods:{
            getNodeData:function () {
                this.$http.get("/lda/getEvolutionData/"+this.nowTopicId).then(function (response) {
                    console.log(response);
                    var evolutionData=response.data;
                    this.nowTopicWeight=evolutionData.weight;
                    this.nowTopicYear=evolutionData.year;
                    this.allTopicWords=evolutionData.topicWords;
                    this.allPaperTitle=evolutionData.topicPaper;
                })

            }

        },
        mounted:function () {
            this.evolutionRelationChart=echarts.init(document.getElementById('evolutionChart'));
            this.nowTopicId=0;
            this.evolutionRelationChart.showLoading();
            this.$http.get("/lda/getJson").then(function (response) {
                json=response.data;
                console.log(json);
                console.log(json.nodes);
                this.evolutionRelationChart.hideLoading();
                this.evolutionRelationChart.setOption(option = {
                    title: {
                        text: '主题演化图',
                        left:300
                    },
                    animationDurationUpdate: 1500,
                    animationEasingUpdate: 'quinticInOut',
                    series : [
                        {
                            type: 'graph',
                            layout: 'none',
                            // progressiveThreshold: 700,
                            data: (json.nodes || []).map(function (node) {
                                console.log("node:"+node);
                                return {
                                    x: node.x,
                                    y: node.y,
                                    id: node.id,
                                    name: node.label,
                                    symbolSize: ((node.size*1300.0)),
                                    // symbolSize: (Math.pow(Math.sqrt((node.size*1800.0-18)*3),2)-10),
                                    itemStyle: {
                                        normal: {
                                            color: node.color
                                        }
                                    }
                                };
                            }),
                            edges: (json.edges || []).map(function (edge) {
                                return {
                                    source: edge.sourceID,
                                    target: edge.targetID
                                };
                            }),
                            label: {
                                position: 'inside',
                                show: true
                                // emphasis: {
                                //     position: 'right',
                                //     show: true
                                // }
                            },
                            edgeSymbol: ['pointer', 'arrow'],
                            edgeSymbolSize: [0, 10],
                            //roam是用来控制缩放和移动的
                            roam: false,
                            focusNodeAdjacency: true,
                            lineStyle: {
                                normal: {
                                    width: 1,
                                    curveness: 0.3,
                                    opacity: 0.7
                                }
                            }
                        }
                    ]
                }, true);
            });
            this.getNodeData();

            this.evolutionRelationChart.on("click",{dataType: 'node'}, function (params){
                console.log("params.name:"+params.name);
                evolutionVue.nowTopicId=params.name;
                evolutionVue.getNodeData();
                // toastr.success("获取主题："+params.name+" 成功!");

            });
        }
    })
}