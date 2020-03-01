window.onload=function () { 
    statistics();
}

function statistics() {
    var statisticsVue=new Vue({
        el:"#statisticsVue",
        data:{
            statisticsDicts:[],
            nowDict:"",
            generalShowChart:"",
            authorKeyShowChart:"",
            venueKeyShowChart:"",
            institutionKeyShowChart:"",
            authorTreeChart:"",
            allKeyWords:[],
            searchResult:[],
            showSearchRes:false,
            nowKeyWord:"",
            nowAuthor:"",
            showTree:false,
            relationList:[],
            authorTotalNum:0

        },
        methods:{
            statisticsDictSelect:function () {
                var statisticsFolder=$("#countDictSelect").val();
                this.nowDict=statisticsFolder;

                this.generalShowChart.showLoading();
                this.authorKeyShowChart.showLoading();
                this.venueKeyShowChart.showLoading();
                this.institutionKeyShowChart.showLoading();

                this.shiftDictFile();


            },
            searchKeyWord:function(){
                var searchWord=$("#searchInput").val();
                console.log("search:"+searchWord);
                var flag=false;
                for(var i=0;i<this.allKeyWords.length;i++){
                    if (this.allKeyWords[i]==searchWord){
                        flag=true;
                        break;
                    }
                }
                if (flag){
                    this.nowKeyWord=searchWord;
                    this.authorKeyShowChart.showLoading();
                    this.venueKeyShowChart.showLoading();
                    this.institutionKeyShowChart.showLoading();
                    this.showThreeAttribute();
                    toastr.success("加载成功！");
                } else {
                    toastr.error("不存在该关键词");
                }
            },
            fuzzySearch:function () {
                this.showSearchRes=true;
                var current = 0;
                $("#searchInput").keyup(function (e) {
                    if($("#searchInput").value!=null){
                        $("#searchInput").value=$("#searchInput").value.replace(/\s/g,"");
                    }
                    if(e.keyCode == 38) {
                        //up
                        console.log('up');
                        current --;
                        if(current <= 0) {
                            current = 0;
                        }
                        console.log(current);
                    }else if(e.keyCode == 40) {
                        //down
                        console.log('down');
                        current ++;
                        if(current >= this.searchResult.length) {
                            current = this.searchResult.length -1;
                        }
                        console.log(current);

                    }else if(e.keyCode == 13) {
                        //enter
                        console.log('enter');
                        //如果按下回车，将此列表项的内容填充到文本框中
                        $("#searchInput").val(this.searchResult[current]);
                        //下拉框隐藏
                        this.showSearchRes=false;
                    }
                    // else if (e.keyCode == 32){
                    //     //other
                    //     console.log('space');
                    //     //文本框中输入的字符串
                    //     var searchValWithTrim = $("#searchInput").val();
                    //     var searchVal=searchValWithTrim.replace(/(^[\s\n\t]+|[\s\n\t]+$)/g, "");
                    //     $("#searchInput").val(searchVal);
                    //     $("#searchInput").focus.click;
                    //     console.log(searchVal.length+":"+searchVal+"---");
                    //     this.searchResult = [];
                    //     //将和所输入的字符串匹配的项存入searchResult
                    //     //将匹配项显示，不匹配项隐藏
                    //     for(var keyword in this.allKeyWords){
                    //         if (keyword.indexOf(searchVal)!=-1){
                    //             this.searchResult.push(keyword);
                    //         }
                    //     }
                    //     console.log(this.searchResult);
                    //     current = 0;
                    // }
                    else {
                        //other
                        console.log('other');
                        //文本框中输入的字符串
                        var searchValWithTrim = $("#searchInput").val();
                        var searchVal=searchValWithTrim.replace(/(^[\s\n\t]+|[\s\n\t]+$)/g, "");
                        console.log(searchVal.length+":"+searchVal+"---");
                        this.searchResult = [];
                        //将和所输入的字符串匹配的项存入searchResult
                        //将匹配项显示，不匹配项隐藏
                        for(var keyword in this.allKeyWords){
                            if (keyword.indexOf(searchVal)!=-1){
                                this.searchResult.push(keyword);
                            }
                        }
                        console.log(this.searchResult);
                        current = 0;
                    }
                })
            },
            showThreeAttribute:function () {
                statisticsVue.$http.get("/statistics/getOneKeyAttribute/"+statisticsVue.nowDict+"/author"+"/"+statisticsVue.nowKeyWord).then(function (authorResponse) {
                    var authorKeyList=authorResponse.data;
                    var authorNameKey=[];
                    var authorNameNum=[];
                    var authorListShowNum=authorKeyList.length<71?authorKeyList.length:70;
                    for(var i=0;i<authorListShowNum;i++){
                        authorNameKey.push(authorKeyList[i].key);
                        authorNameNum.push(authorKeyList[i].num);
                    }
                    statisticsVue.authorKeyShowChart.hideLoading();
                    console.log("开始重绘authorKey图");
                    statisticsVue.authorKeyShowChart.setOption({
                        title: {
                            text: '作者词频分布:'+this.nowKeyWord+"("+authorKeyList.length+")"
                        },
                        xAxis:{
                            data:authorNameKey
                        },
                        series:[
                            {name:'author',type:'line',smooth:true,data:authorNameNum},

                        ]
                    });
                })
                statisticsVue.$http.get("/statistics/getOneKeyAttribute/"+statisticsVue.nowDict+"/venue"+"/"+statisticsVue.nowKeyWord).then(function (venueResponse) {
                    var venueKeyList=venueResponse.data;
                    var venueNameKey=[];
                    var venueNameNum=[];
                    for(var i=0;i<venueKeyList.length;i++){
                        venueNameKey.push(venueKeyList[i].key);
                        venueNameNum.push(venueKeyList[i].num);
                    }
                    statisticsVue.venueKeyShowChart.hideLoading();
                    console.log("开始重绘venueKey图");
                    statisticsVue.venueKeyShowChart.setOption({
                        title: {
                            text: '期刊词频分布:'+this.nowKeyWord
                        },
                        xAxis:{
                            data:venueNameKey
                        },
                        series:[
                            {name:'venue',type:'line',smooth:true,data:venueNameNum},
                        ]
                    });
                })
                statisticsVue.$http.get("/statistics/getOneKeyAttribute/"+statisticsVue.nowDict+"/institution"+"/"+statisticsVue.nowKeyWord).then(function (institutionResponse) {
                    var institutionKeyList=institutionResponse.data;
                    var institutionNameKey=[];
                    var institutionNameNum=[];
                    var institutionListShowNum=institutionKeyList.length<71?institutionKeyList.length:70;
                    for(var i=0;i<institutionListShowNum;i++){
                        institutionNameKey.push(institutionKeyList[i].key);
                        institutionNameNum.push(institutionKeyList[i].num);
                    }
                    statisticsVue.institutionKeyShowChart.hideLoading();
                    console.log("开始重绘institutionKey图");
                    statisticsVue.institutionKeyShowChart.setOption({
                        title: {
                            text: '机构词频分布:'+this.nowKeyWord+"("+institutionKeyList.length+")"
                        },
                        xAxis:{
                            data:institutionNameKey
                        },
                        series:[
                            {name:'institution',type:'line',smooth:true,data:institutionNameNum}
                        ]
                    });
                })

            },
            shiftDictFile:function () {
                statisticsVue.$http.get("/statistics/getAllKeyWords/"+statisticsVue.nowDict).then(function (response) {

                    var resAuthorDict=response.data;
                    var authorKey=[];
                    var authorNum=[];
                    var venueNum=[];
                    var institutionNum=[];

                    statisticsVue.searchResult=resAuthorDict;
                    for(var i=0;i<resAuthorDict.length;i++){
                        var item=resAuthorDict[i];

                        authorKey.push(item.key);
                        authorNum.push(item.num);
                    }
                    console.log(authorKey);
                    statisticsVue.allKeyWords=authorKey;
                    statisticsVue.nowKeyWord=authorKey[0];

                    statisticsVue.generalShowChart.hideLoading();
                    console.log("开始重绘总览图");

                    //避免卡顿，只绘制前70个关键词
                    var authorArry = [];
                    for(var i=0;i<70;i++){
                        var item={
                            name: authorKey[i],
                            value: authorNum[i],
                        }
                        authorArry.push(item);
                    }

                    statisticsVue.generalShowChart.setOption({

                        series:[
                            {name:'keyword',
                                type:'graph',
                                layout: 'force',
                                data:authorArry,
                                roam:true,
                                symbolSize: function (data) {
                                    // console.log(data);
                                    return Math.round(15 + data * 80 / authorNum[0]);
                                },
                                label: {
                                    normal: {
                                        show: true
                                    },
                                    emphasis: {
                                        show: true,
                                        formatter: function (param) {
                                            return param.name;
                                        },
                                        color: 'black'
                                    }
                                },
                                force:{
                                    repulsion: 65
                                }
                            },
                        ]
                    });

                    statisticsVue.showThreeAttribute();

                    // statisticsVue.$http.get("/statistics/getAllKeyWords/"+statisticsVue.nowDict+"/venue").then(function (venueResponse) {
                    //     console.log("获取到了venue数据");
                    //     var venueList=venueResponse.data;
                    //
                    //     for(var i=0;i<authorKey.length;i++){
                    //         for(var j=0;j<venueList.length;j++){
                    //             if (venueList[j].key==authorKey[i]){
                    //                 venueNum.push(venueList[j].num);
                    //                 break;
                    //             }
                    //         }
                    //     }
                    //
                    //     statisticsVue.$http.get("/statistics/getAllKeyWords/"+statisticsVue.nowDict+"/institution").then(function (institutionResponse) {
                    //         console.log("获取到了institution数据");
                    //         var institutionList=institutionResponse.data;
                    //         for(var i=0;i<authorKey.length;i++){
                    //             for(var j=0;j<institutionList.length;j++){
                    //                 if (institutionList[j].key==authorKey[i]){
                    //                     institutionNum.push(institutionList[j].num);
                    //                     break;
                    //                 }
                    //             }
                    //         }
                    //
                    //
                    //
                    //     });
                    //
                    // });

                });
                toastr.success("初始化成功!");
            }
        },
        mounted:function () {
            this.generalShowChart=echarts.init(document.getElementById('generalShowChart'));
            this.authorKeyShowChart=echarts.init(document.getElementById('authorKeyShowChart'));
            this.venueKeyShowChart=echarts.init(document.getElementById('venueKeyShowChart'));
            this.institutionKeyShowChart=echarts.init(document.getElementById('institutionKeyShowChart'));
            // this.authorTreeChart=echarts.init(document.getElementById('authorTreeChart'));

            var categories = ['author','venue','institution'];


            //初始化echarts
            this.generalShowChart.setOption({
                title: {
                    text: '词频总览'
                },
                tooltip: {
                    trigger: 'item'
                },
                legend: {
                    data: categories,
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                // xAxis: {
                //     data: []
                // },
                // yAxis: {},
                series: [{
                    type: 'graph',
                }],
                // dataZoom: [
                //     {   // 这个dataZoom组件，默认控制x轴。
                //         type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
                //         start: 0,      // 左边在 10% 的位置。
                //         end: 1         // 右边在 60% 的位置。
                //     }
                // ],
            });
            this.authorKeyShowChart.setOption({
                title: {
                    text: '作者词频分布:'+this.nowKeyWord
                },
                tooltip: {
                    trigger: 'item'
                },

                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    data: []
                },
                yAxis: {},
                series: [{
                    type: 'line',
                }],
                // dataZoom: [
                //     {   // 这个dataZoom组件，默认控制x轴。
                //         type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
                //         start: 0,      // 左边在 10% 的位置。
                //         end: 1         // 右边在 60% 的位置。
                //     }
                // ],
            });
            this.venueKeyShowChart.setOption({
                title: {
                    text: '期刊词频分布:'+this.nowKeyWord
                },
                tooltip: {
                    trigger: 'item'
                },

                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    data: []
                },
                yAxis: {},
                series: [{
                    type: 'line',
                }],
                // dataZoom: [
                //     {   // 这个dataZoom组件，默认控制x轴。
                //         type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
                //         start: 0,      // 左边在 10% 的位置。
                //         end: 1         // 右边在 60% 的位置。
                //     }
                // ],
            });
            this.institutionKeyShowChart.setOption({
                title: {
                    text: '机构词频分布:'+this.nowKeyWord
                },
                tooltip: {
                    trigger: 'item'
                },
                grid: {
                    left: '3%',
                    right: '4%',
                    bottom: '3%',
                    containLabel: true
                },
                xAxis: {
                    data: []
                },
                yAxis: {},
                series: [{
                    type: 'line',
                }],
                // dataZoom: [
                //     {   // 这个dataZoom组件，默认控制x轴。
                //         type: 'slider', // 这个 dataZoom 组件是 slider 型 dataZoom 组件
                //         start: 0,      // 左边在 10% 的位置。
                //         end: 1         // 右边在 60% 的位置。
                //     }
                // ],
            });



            this.generalShowChart.showLoading();
            this.authorKeyShowChart.showLoading();
            this.venueKeyShowChart.showLoading();
            this.institutionKeyShowChart.showLoading();
            this.$http.get("/statistics/getAllDirections").then(function (response) {
                var dicts=response.data;
                if (dicts==null || dicts.length<1){
                    this.statisticsDicts=[]
                } else{
                    this.statisticsDicts=dicts;
                    this.nowDict=this.statisticsDicts[0].indexName;
                    this.shiftDictFile();

                }

            });

            this.generalShowChart.on("click",{dataType: 'node'}, function (params){
                console.log("params.name:"+params.name);
                statisticsVue.nowKeyWord=params.name;
                statisticsVue.authorKeyShowChart.showLoading();
                statisticsVue.venueKeyShowChart.showLoading();
                statisticsVue.institutionKeyShowChart.showLoading();
                statisticsVue.showThreeAttribute();
                toastr.success("获取关键词："+params.name+" 成功!");

            });

            this.authorKeyShowChart.on("click",function (params) {

                console.log("params.name:"+params.name);
                statisticsVue.nowAuthor=params.name;
                var treeData;

                statisticsVue.$http.get("/statistics/getAuthorPapersTreeData/"+statisticsVue.nowDict+"/"+statisticsVue.nowAuthor).then(function (value) {
                    console.log(value);
                    treeData=value.data;
                });
                statisticsVue.$http.get("/statistics/getRelativeAuthors/"+statisticsVue.nowDict+"/"+statisticsVue.nowAuthor).then(function (value) {
                    console.log(value);
                    if (value.data.length>0){
                        statisticsVue.relationList=value.data;
                    } else{
                        statisticsVue.relationList=[{key:"没有合作作者",num:0}];
                    }
                });
                setTimeout(function () {
                    statisticsVue.authorTreeChart=echarts.init(document.getElementById('authorTreeChart'));

                    statisticsVue.authorTreeChart.setOption({
                        tooltip: {
                            trigger: 'item',
                            triggerOn: 'mousemove'
                        },
                        textStyle: {
                            fontSize: 18
                        },
                        grid: {
                            left: '3%',
                            right: '20%',
                            bottom: '3%',
                            containLabel: true
                        },
                        series: [
                            {
                                type: 'tree',

                                data:[treeData],

                                top: '1%',
                                left: '7%',
                                bottom: '1%',
                                right: '45%',

                                symbolSize: 7,

                                label: {
                                    normal: {
                                        position: 'left',
                                        verticalAlign: 'middle',
                                        align: 'right',
                                        fontSize: 12
                                    }
                                },

                                leaves: {
                                    label: {
                                        normal: {
                                            position: 'right',
                                            verticalAlign: 'middle',
                                            align: 'left',
                                            fontsize: 15
                                        },
                                        textStyle: {
                                            fontsize: 25
                                        }
                                    }
                                },

                                expandAndCollapse: true,
                                animationDuration: 550,
                                animationDurationUpdate: 750
                            }
                        ]
                    });

                },1000)
                $("#authorTreeModal").modal();
                // statisticsVue.showTree=true;
            })

        }
    })
}