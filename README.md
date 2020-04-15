# JDPF
一个Java的数据持久化框架  
## 本框架的特点
非常适合刚刚入门的学习者，因为此框架无需编写sql语句，有点类似于Hibernate框架。但是与Hibernate框架相比，本框架的优势在于方便————使用者只需要创建若干个类，并保证每一个类的类名与待查询表的表名相同，且类中的属性名称也与表中的列名一致，即可查询轻松地数据。而不需要像使用Hibernate框架那样费尽心思去调整与维护数据库表与实体类之间的映射，只需要保证名称相同，本框架会自动地处理各项映射关系。  
## 使用方法  
1.创建若干实体类并设置```Table```为其父类。  
2.实体类类名对应数据库的表名，属性名称对应数据库表中的列名，并且在主键的属性上方或前方写上注解```@PrimaryKey```。  
3.调用```SqlConnector.getSession```获取一个```SqlConnector```类的对象，可以通过修改```JDBC_DRIVER ```属性来设置该框架使用的数据库引擎，但是需要手动添加对应数据库引擎的JAR包。  
4.调用```SqlConnector.select```实现数据库查询，将```Table```对象中标记为```@ParimaryKey```的属性作为where的条件进行查询，查询后的数据会自动填充到```Table```中。  
5.调用```SqlConnector.alter、SqlConnector.delete、SqlConnector.insert```可以分别进行数据修改、删除和插入。  
6.调用```SqlConnector.selectSome、SqlConnector.insertSome```可以查询或插入多条数据。  
