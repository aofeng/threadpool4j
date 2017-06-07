# 1、获取源码。
```bash
git clone https://github.com/aofeng/threadpool4j
```

# 2、编译源码并生成jar
## 方式一：Ant
进入项目根目录，执行ant脚本：
```bash
ant
````
会生成一个`dist`目录，下面有两个文件。如：
<pre>
threadpool4j-3.0.0-src.jar    源码jar
threadpool4j-3.0.0.jar        用于发布的二进制jar
</pre>

## 方式二：Maven
进入项目根目录，执行maven脚本：
```bash
mvn package
```
会生成一个`target`目录，下面有两个文件。如：
<pre>
threadpool4j-3.0.0-src.jar    源码jar
threadpool4j-3.0.0.jar        用于发布的二进制jar
</pre>
