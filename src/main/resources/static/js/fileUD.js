window.onload=function () {
    fileUD();
}

// function uploadFile() {
//     $("#fileForm").ajaxSu
// }

function fileUD() {
    var fileUDVue=new Vue({
        el:"#fileVue",
        data:{
            segFiles:[],
            dictFiles:[{name:"testFile",size:2}],
            nowEditFileName:"",
            editTextAreaModel:"",
            nowTextAreaLineNums:0
        },
        watch:{
            editTextAreaModel:function () {
                var splitLines=this.editTextAreaModel.split("\n");
                this.nowTextAreaLineNums=splitLines.length;
            }
        },
        methods:{
            downLoad:function (dictFile) {

                window.open("/file/download/"+dictFile.name);
            },
            downLoadTimes:function(dictFile){
                this.$http.get("/file/hasTimes/"+dictFile.name).then(function (value) {
                    var responseData=value.data;
                    if (responseData.result=="success"){
                        window.open("/file/downloadTimes/"+dictFile.name);
                        toastr.success(responseData.description);
                    } else{
                        toastr.error(responseData.description);
                    }
                });
            },
            editDict:function(dictFile){
                this.nowEditFileName=dictFile.name;
                // this.$http.get("/dict/getDict/"+this.nowEditFileName).then(function (response) {
                //     if (response.data.length>0){
                //         var allDict=response.data;
                //         var textAreaStr="";
                //         for(var i=0;i<allDict.length;i++){
                //             textAreaStr=textAreaStr+allDict[i]+" "+"10\n";
                //         }
                //         this.editTextAreaModel=textAreaStr;
                //         $("#editDictTextArea").val(textAreaStr);
                //     }
                //     else {
                //         $("#editDictTextArea").val("");
                //     }
                // });
                $("#editDictTextArea").val("");
                this.editTextAreaModel="";
                this.nowTextAreaLineNums=0;
                $("#editDictModal").modal();
            },
            confirmEdit:function(){
                var newEditDict=$("#editDictTextArea").val();
                if (this.nowTextAreaLineNums>200){
                    toastr.error("一次不得超过200行！");
                } else{
                    if (newEditDict.length>0){
                        var dictLines=newEditDict.split("\n");
                        var newDictList=[];
                        for (var j=0;j<dictLines.length;j++){
                            var lineSplit=dictLines[j].split(" ");
                            if (lineSplit[0].length>0){
                                newDictList.push(lineSplit[0]);
                            }
                        }
                        // console.log("list-newEditDict:"+newDictList);
                        // this.$http.get("/dict/modifyDictFile/"+this.nowEditFileName+"/"+newDictList)
                        this.$http.get("/dict/modifyDictFile/"+this.nowEditFileName+"/"+newDictList)
                            .then(function (value) {
                                console.log(value);
                                // toastr.success("success");
                                var responseData=value.data;
                                if (responseData.result=="success"){
                                    toastr.success(responseData.description);
                                } else{
                                    toastr.error(responseData.description);
                                }
                                $("#editDictModal").map(function () {
                                    if (!$(this).is(":hidden")){
                                        $(this).modal('hide');
                                    }
                                });
                            })
                            .catch(function (e) {
                                console.log(e);
                                toastr.error("错误"+e.status+":"+e.statusText);
                            });
                    }else{
                        toastr.warning("至少输入一组分词词典!");
                    }
                }

            },
            downLoadSource:function (dictFile) {

                window.open("/file/downloadSource/"+dictFile.name);
            },
            uploadFile:function () {
                var fileName=$("#segmentFileInput").val();
                fileNameSplit=fileName.split("\\");
                if (fileNameSplit.length==1){
                    fileNameSplit=fileName.split("/");
                }
                fileName=fileNameSplit[fileNameSplit.length-1];
                console.log("要上传的文件名："+fileName);
                hasSameName=false;
                if(this.segFiles.length>0){
                    for(i=0;i<this.segFiles.length;i++){
                        if (this.segFiles[i]==fileName){
                            hasSameName=true;
                            break;
                        }
                    }
                }
                if(hasSameName){
                    $("#confirmCoverModal").modal();
                }else {
                    $("#fileForm").ajaxSubmit(function (response) {
                        console.log(response)
                        if (response=="success"){
                            toastr.success("上传成功!");
                            console.log("开始更新文件列表")
                            fileUDVue.$http.get("/file/getAllDictFiles").then(function (response) {
                                console.log(response);
                                if(response.data!=null && response.data.length>0){
                                    this.dictFiles=response.data;
                                    document.getElementById("showNo").style.display="none";
                                }else{
                                    this.dictFiles=[];
                                    document.getElementById("showNo").style.display="block";
                                }
                            });

                        }else {
                            toastr.error("上传失败!")
                        }
                        $(this).resetForm();
                        $(this).clearForm();
                    });
                    return false;
                }
            },
            confirmCover:function () {
                $("#confirmCoverModal").map(function () {
                    if (!$(this).is(":hidden")){
                        $(this).modal('hide');
                    }
                });
                $("#fileForm").ajaxSubmit(function (response) {
                    console.log(response)
                    if (response=="success"){
                        toastr.success("上传成功!");
                        console.log("开始更新文件列表")
                        fileUDVue.$http.get("/file/getAllDictFiles").then(function (response) {
                            console.log(response);
                            if(response.data!=null && response.data.length>0){
                                this.dictFiles=response.data;
                                document.getElementById("showNo").style.display="none";
                            }else{
                                this.dictFiles=[];
                                document.getElementById("showNo").style.display="block";
                            }
                        });

                    }else {
                        toastr.error("上传失败!")
                    }
                    $(this).resetForm();
                    $(this).clearForm();
                });
                return false;
            }

        },
        mounted:function () {
            this.$http.get("/file/getAllSegmentFiles").then(function (response) {
                if(response.data!=null && response.data.length>0){
                    this.segFiles=response.data;
                }else{
                    this.segFiles=[];
                }
            })
            this.$http.get("/file/getAllDictFiles").then(function (response) {
                if(response.data!=null && response.data.length>0){
                    this.dictFiles=response.data;
                }else{
                    this.dictFiles=[];
                    document.getElementById("showNo").style.display="block";
                }
            })
        }
    })
}