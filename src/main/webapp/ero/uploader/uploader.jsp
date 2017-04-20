<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <script type="text/javascript" src="/com-res/common/js/jquery-1.12.4.min.js" ></script>
</head>
<body>
<form id="upload_form" method="post" action="/navigator/uploader" enctype="multipart/form-data">
    <input type="hidden" id="uploader_type" name="type" value="upload">
    <input type="hidden" id="uploader_reqId" name="reqId">
    <input type="hidden" id="uploader_fileType" name="fileType">
    <input type="hidden" id="uploader_fileMaxSize" name="fileMaxSize">
    <input type="file" id="uploader_input" name="uploader_input" onchange="upload()">
</form>
<div id="upload_fileNames"><%=request.getAttribute("fileNames")%></div>
<div id="upload_code"><%=request.getAttribute("code")%></div>
<div id="upload_message"><%=request.getAttribute("message")%></div>
</body>
<script type="text/javascript">
    function upload() {
        var files = document.getElementById("uploader_input").files;
        if (files.length >0) {
            var fileTypes = document.getElementById("uploader_fileType").value.split(",");
            var fileMaxSize = parseInt(document.getElementById("uploader_fileMaxSize").value);
            var file;
            for (var i=0; i<files.length; i++) {
                file = files[i];
                if ((fileTypes[0] != "" && fileTypes.indexOf(file.name.slice(file.name.lastIndexOf(".")+1)) == -1) || (fileMaxSize != "" && file.size > fileMaxSize)) {
                    window.location.reload();
                }else {
                    document.getElementById("upload_form").submit();
                }
            }
        }
    }
</script>
</html>
