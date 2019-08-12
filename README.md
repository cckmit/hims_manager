

项目开发介绍
---

- lemon security需引用

  ```java
  lemon-framework-starter-session
  lemon-framework-starter-security
  lemon-framework-starter-security-refresh
  ```

- lemon framework会忽略的配置

  ```java
  lemon.acc.enabled
  lemon.security.principal.principalNameExpression  认证用户名
  lemon.security.authentication.loginPathPrefix     默认/security/login
  lemon.security.authentication.refreshPath         默认/security/refresh
  lemon.security.authentication.logoutPath          默认/security/logout
  lemon.security.authorizeRequests.permitAll        不需要认证检查的url地址，格式[- /operation/base/v1/login]
  ```

- InternalDataHelper，需定义在com.cmpay.lemon目录下才能使用

- #### 路由配置

  前端项目中src/router/index.js的路由表中每个具有实际页面的路由都有一个角色唯一标识:

  ![ZVByLt.png](https://s2.ax1x.com/2019/06/25/ZVByLt.png)

用户登录后，会从服务端获取到一张当前用户的权限表,对应数据库中sys_menu表的记录：

![ZVDkFO.png](https://s2.ax1x.com/2019/06/25/ZVDkFO.png)

![ZVDQTf.png](https://s2.ax1x.com/2019/06/25/ZVDQTf.png)

通过这张表上的perms字段，就可以和路由表中的标识进行匹配,匹配上的路由将会在菜单栏显示,没匹配到则不显示.

- #### 页面按钮权限

  通过自定义方法isAuth()来查找当前用户有没有上面报文中的permissions中的某项权限来控制:

  [![ZVrGE6.png](https://s2.ax1x.com/2019/06/25/ZVrGE6.png)](https://imgchr.com/i/ZVrGE6)
