#stage
springboot+mybatis 面向接口（api）简易小demo，初学者也能快速上手

RESTful架构，结构清晰，传参灵活
##目录结构
```
java:
    plugin（常用工具类）
resoureces:
    config.yaml（统一环境配置文件）
    message.yaml (统一状态码配置文件）
```
##RESTful API curl
```
添加用户：
curl --location --request POST 'http://localhost:8080/v1/user' \
--header 'action: add' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name":"tout",
    "password":"123"
}'

用户列表：
curl --location --request GET 'http://localhost:8080/v1/user?page=1&size=10' \
--header 'action: list'

用户详情：
curl --location --request GET 'http://localhost:8080/v1/user?id=1' \
--header 'action: info'

修改用户:
curl --location --request PUT 'http://localhost:8080/v1/user' \
--header 'action: update' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id":1,
    "password":"123456"
}'

删除用户：
curl --location --request DELETE 'http://localhost:8080/v1/user' \
--header 'action: delete' \
--header 'Content-Type: application/json' \
--data-raw '{
    "id":1
}'
```
注：一个router下的header action不允许重复！控制器通过action定位方法，联调时也可以通过action快速定位接口

##返参示例
```
{
    "status": 10000,
    "msg": "请求成功",
    "body": null
}
```

##测试user表结构
```
CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(55) COLLATE utf8mb4_general_ci NOT NULL,
  `password` varchar(55) COLLATE utf8mb4_general_ci NOT NULL,
  `status` tinyint unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
```

没了，懂得都懂，不懂的说了也不懂...╮(╯▽╰)╭