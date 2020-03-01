window.onload=function (ev) {
    tag();
}

function tag() {
    var tagVue=new Vue({
        el:"#tagVue",
        data:{
            hotShow:true,
            tagInfos:[{title:"正在加载标注文件...",tags:["正在加载标注..."]}],
            specialTagInfos:[{title:"正在加载标注文件...",tags:["正在加载标注..."]}],
            //oneTag：被选中添加tag的那条
            oneTag:[{title:"正在加载标注文件...",tags:["正在加载标注..."]}],
            inputNumber:1,
            input:"input",
            addInput:"addInput",
            newTagText:"新标签",
            hotButtons:[],
            allTags:["tag1","tag2"],
            nowTag:"",
            allShow:true,
            totalPage:1,
            lastEnterDate:"",
            nowPage:1,
            nowIndex:"history"
        },
        methods:{
            chooseAddTag:function (tagInfo) {
                this.inputNumber=1;
                $("#input1").val("");
                this.oneTag=tagInfo;
                $("#addModal").modal();
                $("#titleAddTagButton").attr("disabled",false);
            },
            showHot:function(){
                this.hotShow=!this.hotShow;
            },
            preTagPage:function(){
                if (this.nowPage==1){
                    toastr.warning("已经是第一页了！");
                }else{
                    this.nowPage=this.nowPage-1;
                    $("#pageInput").val(this.nowPage);
                    this.getTagsByPage(this.nowPage);
                }
            },
            postTagPage:function(){
                if (this.nowPage==this.totalPage){
                    toastr.warning("已经是最后一页了！")
                } else{
                    this.nowPage=this.nowPage+1;
                    $("#pageInput").val(this.nowPage);
                    this.getTagsByPage(this.nowPage);
                }
            },
            choosePage:function(){
                // this.showPageList=true;
                $("#pageInput").keyup(function (e) {
                    if (e.keyCode==13){
                        if( (new Date()).getTime() - (tagVue.lastEnterDate).getTime() < 500 ){
                            console.log("长按，0.5秒后才可以移动一次");
                            return;
                        }
                        tagVue.lastEnterDate=new Date();
                        $("#pageList").hide();
                        var inputPageContent=$("#pageInput").val();
                        if (inputPageContent.length<1){
                            toastr.error("请求的页数不合理:"+inputPageContent);
                        }else if (inputPageContent=="" ) {
                            toastr.error("请求的页数不得为空:"+inputPageContent);
                        }else if(parseInt(inputPageContent)>tagVue.totalPage){
                            toastr.error("请求的页数不存在:"+inputPageContent);
                        }else if (parseInt(inputPageContent)<0) {
                            toastr.error("请求的页数不得为负数:"+inputPageContent);
                        }
                        else{
                            if (parseInt(inputPageContent)==tagVue.nowPage){
                                toastr.success("切换到页"+inputPageContent+"成功!");
                            } else{
                                // segVue.showPageList=false;
                                this.nowPage=parseInt(inputPageContent);
                                $("#pageInput").val(tagVue.nowPage);
                                tagVue.getTagsByPage(inputPageContent);
                            }

                        }
                    }
                    return;
                });

            },
            getTagsByPage:function(pageNum){
                this.nowPage=parseInt(pageNum);
                $("#pageInput").val(this.nowPage);
                // toastr.success("切换到页："+pageNum);
                this.$http.get("/tags/getTagByPage/"+this.nowIndex+"/"+pageNum).then(function (pageResponse) {
                    console.log(pageResponse);
                    this.tagInfos=[];
                    var responseData=pageResponse.data;
                    if (responseData.length<101 && responseData.length>0){
                        this.tagInfos=responseData;
                    } else if (responseData.length>100) {
                        for(var i=0;i<100;i++){
                            (this.tagInfos).push(responseData[i]);
                        }
                        setTimeout(function () {
                            for(var j=100;j<responseData.length;j++){
                                (tagVue.tagInfos).push(responseData[j]);
                            }
                        },1000);
                        toastr.success("切换到第"+pageNum+"页成功!");
                    }else{
                        toastr.warning("sorry,第"+pageNum+"页内容消失!");
                    }
                });
            },
            getTagData:function(tag){
              this.$http.get("/tags/getTagInfosByTag/"+this.nowIndex+"/"+tag)
                  .then(function (value) {
                      if (value.data.length>0){
                          this.specialTagInfos=value.data;
                          this.nowTag=tag;
                          this.allShow=false;
                          $("#searchTagInput").val("");
                          $("#showAllButton").attr("disabled",false);
                      } else{
                          toastr.error("不存在该标签")
                      }

                  })
                  .catch(function (e) {
                  console.log(e);
                  toastr.error("错误"+e.status+":"+e.statusText);
              });


            },
            searchTag:function(){
                var tag=$("#searchTagInput").val();
                var exist=false;
                // for(var i=0;i<this.allTags.length;i++){
                //     if (this.allTags[i]==tag){
                //         exist=true;
                //         break;
                //     }
                // }
                if (tag.length>0){
                    exist=true;
                }
                if (exist){
                    this.getTagData(tag);
                } else{
                    toastr.error("不存在该标签");
                }
            },
            showAll:function(){

                $("#showAllButton").attr("disabled",'disabled');
                if (this.allShow==false){
                  this.allShow=true;
                  toastr.success("已加载全部");
                }else{
                  toastr.warning("已经是全部标签内容");
                }
            },
            bulkTagsAdd:function () {
                var originString=$("#tagOriginsTextArea").val();
                var tagString=$("#bulkTagsInputText").val();
                if (originString!=null && tagString!=null && originString.length>0 && tagString.length>0){
                    var originStringList=originString.split(/[,/，;；。.]/);
                    var transferTagInfo=tagString+":";
                    var hasOrigin=false;
                    for (var i=0;i<originStringList.length;i++){
                        var eachListElement=originStringList[i];
                        if (eachListElement.length>0){
                            hasOrigin=true;
                            transferTagInfo=transferTagInfo+eachListElement+"-";
                        }
                    }
                    if (hasOrigin){
                        console.log(transferTagInfo);
                        this.$http.get("/tags/bulkAddTags/"+this.nowIndex+"/"+transferTagInfo).then(
                            function (value) {
                                toastr.success("添加成功");
                                $("#tagOriginsTextArea").val("");
                                $("#bulkTagsInputText").val("");
                            }
                        );
                    } else{
                        toastr.warning("被标注内容全为分隔符！");
                    }

                }else{
                    toastr.error("标签和标注的词都不得为空");
                }
            },
            addInputDom:function () {
                this.inputNumber=this.inputNumber+1;
            },
            hideInput:function (n) {
                if (this.inputNumber==1){
                    toastr.error("不可少于一个输入框")
                }else{
                    this.inputNumber=this.inputNumber-1;
                }
                // $("#addInput"+''+n).hide();
                // $("#input"+''+n).val("");
            },
            confirmAddTags:function () {
                $("#titleAddTagButton").attr("disabled","disabled");
                var hasTag=false;
                var tagInfoString=this.oneTag.title+":";
                var newTags=[];
                for(var i=0;i<this.inputNumber;i++){
                    var index=i+1;
                    var tagInputI=$("#input"+''+index).val();
                    if (tagInputI!=null && tagInputI.length>0){
                        hasTag=true;
                        tagInfoString=tagInfoString+tagInputI+"-";
                        newTags.push(tagInputI);
                    }
                }
                console.log(tagInfoString);
                if (hasTag){
                    this.$http.get("/tags/addTitleTag/"+this.nowIndex+"/"+tagInfoString).then(function (value) {
                        for(var j=0;j<this.tagInfos.length;j++){
                            if (this.tagInfos[j].title==this.oneTag.title){
                                for(var k=0;k<newTags.length;k++){
                                    this.tagInfos[j].tags.push(newTags[k]);
                                }
                            }
                        }
                        this.inputNumber=1;
                        $("#input1").val("");
                        $("#addModal").map(function () {
                            if (!$(this).is(":hidden")){
                                $(this).modal('hide');
                            }
                        });
                        toastr.success("添加成功！")
                    })
                } else{
                    toastr.error("至少存在一个分词！");
                }
                $("#titleAddTagButton").attr("disabled",false);

            },
            firstGetTagInfo:function () {
                this.$http.get("/tags/getAllTags/"+this.nowIndex)
                    .then(function (allResponse) {
                        var allResponseData=allResponse.data;
                        var pageNum=allResponseData.pageNum;
                        this.totalPage=pageNum;
                        this.nowPage=1;
                        $("#pageInput").val(this.nowPage);

                        this.tagInfos=[];
                        var responseData=allResponseData.pageData;
                        console.log(responseData.length);
                        if (responseData.length<100){
                            this.tagInfos=responseData;
                            toastr.success("加载成功");
                        } else{
                            for(var i=0;i<100;i++){
                                (this.tagInfos).push(responseData[i]);
                            }
                            setTimeout(function () {
                                for(var j=100;j<responseData.length;j++){
                                    (tagVue.tagInfos).push(responseData[j]);
                                }
                                toastr.success("加载成功");
                            },1000);
                        }
                        // this.tagInfos=value.data;
                    })
                    .catch(function (e) {
                        console.log(e);
                        toastr.error("错误"+e.status+":"+e.statusText);
                    });
            }
        },
        mounted:function () {
            this.lastEnterDate=new Date();
            this.firstGetTagInfo();
            this.$http.get("/tags/getTagList/"+this.nowIndex).then(function (value) {
                this.allTags=value.data;
                if (this.allTags.length>10){
                    this.hotButtons=[];
                    for(var i=0;i<10;i++){
                        this.hotButtons.push(this.allTags[i]);
                    }
                }else{
                    this.hotButtons=this.allTags;
                }
            });
        }
    })
}