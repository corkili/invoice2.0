<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 李浩然
  Date: 2017/8/11
  Time: 16:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Test</title>
</head>
<body>
    <div>
        <form action="test" method="post" enctype="multipart/form-data">
            <input type="file" name="file" id="file">
            <input type="submit" value="点此上传">
        </form>
    </div>
    <div>
        <p>${type}</p>
        <br/>
        <p>${originName}</p>
        <br/>
        <p>${name}</p>
        <br/>
        <p>${base64}</p>
    </div>
</body>
</html>
