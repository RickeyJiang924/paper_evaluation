
window.onload=function () {
    segmentTrain();
}


function segmentTrain() {
    var segVue=new Vue({
        el:"#segmentVue",
        data:{
            segmentFiles:["没有文件"],
            nowSegmentFile:"",
            segments:[{title:"正在加载分词文件...",segments:["请稍等..."]}],
            oneSeg:"",
            newName:"",
            myselfSegments:["CSSCI"],
            deleteSegment:"",
            inputNumber:1,
            input:"input",
            addInput:"addInput",
            showSegPartEC:"",
            showRealSeg:true,
            // inputPageNum:1,
            nowPage:1,
            totalPage:1,
            enableShiftPage:false,
            showPageDom:true,
            lastEnterDate:""
            // showPageList:false
        },
        // watch:{
        //     inputPageNum :function(val,oldVar){
        //         if (val>segVue.totalPage || val==""){
        //             segVue.enableShiftPage=false;
        //             toastr.warning("不存在该页");
        //         }else{
        //             segVue.nowPage=val;
        //             segVue.enableShiftPage=true;
        //             // segVue.getSegmentsByPage(segVue.nowPage);
        //         }
        //     }
        // },
        methods:{
            segSelect:function () {
                console.log("切换了分词文件");
                this.showRealSeg=false;
                console.log("隐藏真正的分词文件"+this.showRealSeg);
                this.segments=[];
                this.segments = [{title:"正在加载分词文件...",segments:["请稍等..."]}];
                $("#reRunButton").attr("disabled",true);
                this.nowSegmentFile=$("#segFileSelect").val();
                console.log("切换文件，新文件请求URL：/seg/getSegments/"+this.nowSegmentFile);
                this.getAllSegments();
                // this.$http.get("/dict/getDict").then(function (response) {
                this.$http.get("/dict/getDict/"+this.nowSegmentFile).then(function (response) {
                    if (response.data.length>0){
                        this.myselfSegments=response.data;
                    }
                    else {
                        this.myselfSegments=[];
                    }
                });
                // toastr.success("加载文件"+this.nowSegmentFile+"成功")
            },
            getDictLog:function(){
                this.$http.get("/file/getDictLog/"+this.nowSegmentFile).then(function (response) {
                    console.log(response);
                    if (response.bodyText=="NOFILE"){
                        toastr.error("该文件暂未生成ReRun日志");
                    } else{
                        nwin = window.open("about:blank","_blank"); //新开空白标签页
                        nwin.document.write(response.bodyText); //将内容写入新标签页
                        nwin.focus(); //获取焦点
                        nwin.document.title="日志文件-"+this.nowSegmentFile;

                    }
                }).catch(function (e) {
                    console.log(e);
                    toastr.error("错误"+e.status+":"+e.statusText);
                });
            },
            rerun:function(){
                $("#reRunButton").attr("disabled","disabled");
              this.$http.get("/seg/rerun/"+this.nowSegmentFile)
                  .then(function (response) {
                      console.log("rerun console:");
                      console.log(response);
                  if (response.bodyText=="success"){
                      toastr.success("重新分词成功!");
                      this.getAllSegments();
                  } else{
                      toastr.error(response.bodyText);
                  }
                      $("#reRunButton").attr("disabled",false);
              })
            },
            prePage:function(){
              if (this.nowPage==1){
                  toastr.warning("已经是第一页了！");
              }else{
                  this.nowPage=this.nowPage-1;
                  $("#pageInput").val(this.nowPage);
                  this.getSegmentsByPage(this.nowPage);
              }
            },
            postPage:function(){
                if (this.nowPage==this.totalPage){
                    toastr.warning("已经是最后一页了！")
                } else{
                    this.nowPage=this.nowPage+1;
                    $("#pageInput").val(this.nowPage);
                    this.getSegmentsByPage(this.nowPage);
                }
            },
            choosePage:function(){
                // this.showPageList=true;
                $("#pageInput").keyup(function (e) {
                    if (e.keyCode==13){
                        if( (new Date()).getTime() - (segVue.lastEnterDate).getTime() < 500 ){
                            console.log("长按，0.5秒后才可以移动一次");
                            return;
                        }
                        segVue.lastEnterDate=new Date();
                        $("#pageList").hide();
                        var inputPageContent=$("#pageInput").val();
                        if (inputPageContent.length<1){
                            toastr.error("请求的页数不合理:"+inputPageContent);
                        }else if (inputPageContent=="" ) {
                            toastr.error("请求的页数不得为空:"+inputPageContent);
                        }else if(parseInt(inputPageContent)>segVue.totalPage){
                            toastr.error("请求的页数不存在:"+inputPageContent);
                        }else if (parseInt(inputPageContent)<0) {
                            toastr.error("请求的页数不得为负数:"+inputPageContent);
                        }
                        else{
                            if (parseInt(inputPageContent)==segVue.nowPage){
                                toastr.success("切换到页"+inputPageContent+"成功!");
                            } else{
                                // segVue.showPageList=false;
                                this.nowPage=parseInt(inputPageContent);
                                $("#pageInput").val(this.nowPage);
                                segVue.getSegmentsByPage(inputPageContent);
                            }

                        }
                    }
                    return;
                });

            },
            chooseAddSeg:function (segment) {
                this.oneSeg=segment;
                this.newName="";
                $("#addModal").modal();
            },
            confirmModify:function () {
                var canTransfer=true;
                var hasNewSeg=false;
                var newSegs=[];
                for(i=1;i<=this.inputNumber;i++){
                    var inputI=$("#input"+''+i).val();
                    if (inputI!="" && inputI!=null && inputI.length>0){
                        hasNewSeg=true;
                        var hasNoDuplicate=true;
                        //检测重复
                        if(newSegs.length>0){
                            var j=0;
                            for(j=0;j<newSegs.length;j++){
                                if(newSegs[j]==inputI){
                                    hasNoDuplicate=false;
                                    canTransfer=false;
                                    break;
                                }
                            }
                        }
                        if (hasNoDuplicate){
                            newSegs.push(inputI);
                        }else {
                            toastr.error("存在重复词："+inputI);
                        }
                    }
                    if(!canTransfer){
                        break;
                    }
                }
                if(canTransfer){
                    if(newSegs.length>0){
                        console.log("新的分词列表:"+newSegs);
                        this.$http.post("/dict/addDictList/"+this.nowSegmentFile+"/"+newSegs);
                        var k=0;
                        for(k=0;k<newSegs.length;k++){
                            this.myselfSegments.push(newSegs[k]);

                        }
                        $("#addModal").map(function () {
                            if (!$(this).is(":hidden")){
                                $(this).modal('hide');
                            }
                        });
                        this.inputNumber=1;
                        $("#input1").val("");
                        toastr.success("已成功添加新的分词！")
                    }else{
                        toastr.error("至少添加一个新的分词！")
                    }
                }


            },
            changeInput:function () {
                console.log("开始选择")
                console.log("选中文字："+window.getSelection?window.getSelection():document.selection.createRange().text);
                $("#singleInputText").val(window.getSelection?window.getSelection():document.selection.createRange().text);
            },
            singleSegAdd:function () {
                var singleSegName=$("#singleInputText").val();
                if(singleSegName==""){
                    toastr.error("分词不得为空！")

                }
                else{
                    console.log("添加单独分词:"+singleSegName);
                    var flag=true;
                    for(i=0;i<this.myselfSegments.length;i++){
                        if(this.myselfSegments[i]==singleSegName){
                            flag=false;
                        }
                    }
                    if(flag){
                        this.$http.post("/dict/addDict/"+this.nowSegmentFile+"/"+singleSegName)
                            .then(function () {
                                this.myselfSegments.push(singleSegName);
                                $("#singleInputText").val("");
                                toastr.success("已成功添加新的分词！");
                            })
                            .catch(function (e) {
                            console.log(e);
                            toastr.error("错误"+e.status+":"+e.statusText);
                        });

                    }else {
                        toastr.warning("已经存在该分词")
                    }

                }
            },
            deleteSeg:function (mySeg) {
                this.deleteSegment=mySeg;
                $("#deleteModal").modal();
                // alert("sure to delete the segment:"+mySeg)
            },
            confirmDelete:function () {
                this.$http.post("/dict/delete/"+this.nowSegmentFile+"/"+this.deleteSegment);
                // this.$http.post("http://localhost:8080/dict/delete/"+this.deleteSegment);
                var newMyselfSegs=[];
                for(i=0;i<this.myselfSegments.length;i++){
                    if(this.myselfSegments[i]!=this.deleteSegment){
                        newMyselfSegs.push(this.myselfSegments[i]);
                    }
                }
                this.myselfSegments=newMyselfSegs;
                $("#deleteModal").map(function () {
                    if (!$(this).is(":hidden")){
                        $(this).modal('hide');
                    }
                });
            },
            addInputDom:function () {
                this.inputNumber=this.inputNumber+1;
            },
            hideInput:function (n) {
                $("#addInput"+''+n).hide();
                $("#input"+''+n).val("");
            },
            getAllSegmentsData:function () {
                segVue.$http.get("/seg/getPreSegments/"+segVue.nowSegmentFile).then(function (preResponse){
                    segVue.segments=[];
                    if (preResponse.data.length>0){
                        segVue.segments=preResponse.data;
                        setTimeout(function () {
                            segVue.showRealSeg=true;
                            console.log("显示真正的分词文件"+segVue.showRealSeg);
                        },500);
                        if (segVue.segments.length>98){
                            segVue.$http.get("/seg/getSegments/"+segVue.nowSegmentFile).then(function (response) {
                                console.log("开始加载剩余dict");
                                if(response.data!=null && response.data.length>0){
                                    var responseData=response.data;
                                    var oneTimeShowNum=500;
                                    var pushTime=Math.floor(responseData.length/oneTimeShowNum);
                                    console.log("需要加载的完整次数:"+pushTime);
                                    setTimeout(function () {
                                        if (pushTime>0){
                                            for(var indexNum=0;indexNum<pushTime;indexNum++){
                                                (function (indexNum) {
                                                    setTimeout(function () {
                                                        var begin=indexNum*oneTimeShowNum;
                                                        var end=begin+oneTimeShowNum;
                                                        for(var temp=begin;temp<end;temp++){
                                                            (segVue.segments).push(responseData[temp]);
                                                        }
                                                        indexNum++;
                                                        console.log("加载次数："+indexNum+"时间:"+(new Date().toLocaleString()));
                                                    },500);
                                                })(indexNum)
                                            }
                                        }
                                        var lastRest=responseData.length%oneTimeShowNum;
                                        if (lastRest>0){
                                            var restBegin=pushTime*oneTimeShowNum;
                                            var restEnd=responseData.length;
                                            for(var restTemp=restBegin;restTemp<restEnd;restTemp++){
                                                (segVue.segments).push(responseData[restTemp]);
                                            }
                                        }


                                        $("#reRunButton").attr("disabled",false);
                                        toastr.success("加载文件"+segVue.nowSegmentFile+"成功");
                                    },3000);

                                }
                            });

                        }
                    } else{
                        segVue.segments=[{title:"分词文件为空",segments:["请通知管理人员上传分词文件"]}];
                    }

                });
            },
            getSegmentsByPage:function (pageNum) {
                this.nowPage=parseInt(pageNum);
                $("#pageInput").val(this.nowPage);
                this.showRealSeg=false;
                // toastr.success("切换到页："+pageNum);
                this.$http.get("/seg/getPageSegments/"+segVue.nowSegmentFile+"/"+pageNum).then(function (allResponse) {
                    console.log(allResponse);
                    this.segments=[];
                    var allResponseSegList=allResponse.data;
                    if (allResponseSegList.length>0 && allResponseSegList.length<101){
                        this.segments=allResponseSegList;
                        toastr.success("切换到第"+pageNum+"页成功!");
                        this.showRealSeg=true;
                    } else if (allResponseSegList.length>100) {
                        for(var i=0;i<100;i++){
                            (this.segments).push(allResponseSegList[i]);
                        }
                        this.showRealSeg=true;
                        setTimeout(function () {
                            for(var i=100;i<allResponseSegList.length;i++){
                                (segVue.segments).push(allResponseSegList[i]);
                            }
                            toastr.success("切换到第"+pageNum+"页成功!");
                        },1000);
                    }else{
                        toastr.warning("sorry,该页内容消失!");
                        // segVue.segments=[{title:"分词文件为空",segments:["请通知管理人员上传分词文件"]}];
                        this.showRealSeg=true;
                    }
                });
            },
            getAllSegments:function () {
                this.$http.get("/seg/getSegments/"+segVue.nowSegmentFile).then(function (allResponse) {
                    console.log(allResponse);
                    this.segments=[];
                    var allResponseData=allResponse.data;
                    var pageNum=allResponseData.pageNum;
                    this.totalPage=pageNum;
                    this.nowPage=1;
                    $("#pageInput").val(this.nowPage);
                    if (pageNum==1){
                        this.showPageDom=false;
                    } else {
                        this.showPageDom=true;
                    }
                    var allResponseSegList=allResponseData.pageData;
                    if (allResponseSegList.length>0 && allResponseSegList.length<101){
                        this.segments=allResponseSegList;
                        toastr.success("加载"+segVue.nowSegmentFile+"成功!");
                        $("#reRunButton").attr("disabled",false);
                        this.showRealSeg=true;
                    } else if (allResponseSegList.length>100) {
                        for(var i=0;i<100;i++){
                            (this.segments).push(allResponseSegList[i]);
                        }
                        this.showRealSeg=true;
                        setTimeout(function () {
                            for(var i=100;i<allResponseSegList.length;i++){
                                (segVue.segments).push(allResponseSegList[i]);
                            }
                            toastr.success("加载"+segVue.nowSegmentFile+"成功!");
                            $("#reRunButton").attr("disabled",false);
                        },1000);
                    }else{
                        segVue.segments=[{title:"分词文件为空",segments:["请通知管理人员上传分词文件"]}];
                        this.showRealSeg=true;
                    }
                });
            }
        },
        mounted:function () {
            this.lastEnterDate=new Date();
            this.nowPage=1;
            this.totalPage=5;
            $("#pageInput").val(this.nowPage);
            $("#reRunButton").attr("disabled",true);
            this.$http.get("/file/getAllSegmentFiles").then(function (response) {
                    if (response.data!=null && response.data.length>0){
                        this.segmentFiles=response.data;
                        this.nowSegmentFile=this.segmentFiles[0];

                        console.log("mounted:/seg/getSegments/"+this.nowSegmentFile)
                        // this.$http.get("/seg/getSegments").then(function (response) {
                        this.getAllSegments();
                        // this.$http.get("/dict/getDict").then(function (response) {
                        segVue.$http.get("/dict/getDict/"+segVue.nowSegmentFile).then(function (response) {
                            if (response.data.length>0){
                                segVue.myselfSegments=response.data;
                            }
                            else {
                                segVue.myselfSegments=[];
                            }
                        });
                    }
                    else {
                        this.segments=[{title:"没有分词文件",segments:["请通知管理人员上传分词文件"]}];
                    }
                }).catch(function (e) {
                console.log(e);
                toastr.error("错误"+e.status+":"+e.statusText);
            });
        }
    });
    return segVue;
}