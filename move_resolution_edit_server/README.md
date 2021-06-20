# spring-bootstrap
 曹老师的课程,第二次作业。
 a:水平扩展,20并发访问。(分别对应1台服务器vs4台服务器）
![image](https://github.com/tangjianback/spring-bootstrap/blob/main/CD23CA5CE081257C221B02D194644606.jpg)
![image](https://github.com/tangjianback/spring-bootstrap/blob/main/09BDDD2DBF8CF3E87D139A484ABA7418.jpg)
b:有无redis，20并发访问一个服务器(分别对应有redis,无redis）

![image](https://github.com/tangjianback/spring-bootstrap/blob/main/3249608000E9F2639BCAEFE475F0C068.jpg)
![image](https://github.com/tangjianback/spring-bootstrap/blob/main/AEA0E0A32ECE90E8E81C3B190685F109.jpg)


注:实验中，使用的简单的数据库类型，已经是全部加载如入内存再进行增删改查，所以使用redis的查询效果并不快于初始的情况。如果换成mysql，redis性能提升明显。
