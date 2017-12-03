## 介绍

 * 一个简单的接口测试工具Demo.(用于简单的功能性测试)
 


## 开发背景

 * 工作中，在系统测试阶段常常需要调试接口的连通性以及返回数据的正确性。随着系统开发时间推移，接口的数量激增，如果仅仅依靠人工测试费时，所以使用java写了一个简单小工具提高工作效率。
 
 
## 思路

 * 使用excel表，按照“接口测试用例表”的方式罗列出待测数据，通过程序去根据每个接口的请求地址、请求方式（本demo只写了get、post方式）以及请求数据的不同来拼接发送请求，然后用期望数据在http请求返回的数据中进行查找比较，如果符合预期，备注“通过”；如果与预期不符，则备注原因。

* 网上找的测试的接口的内容已经停掉了，但依然有返回，不影响效果展示。
![Image text](https://raw.githubusercontent.com/ericyishi/img-folder/master/checkInterface01.png)
                                    <p>图1：接口测试用例表</p>
                                    
 ## 文件结构 
 
 ![Image text](https://raw.githubusercontent.com/ericyishi/img-folder/master/checkInterface02.png)
                                    <p>图2：项目文件结构</p>

* App.java为主运行文件，这里只需要传入被测excel文件的`路径`和`测试人姓名`即可。


## 运行结果

 ![Image text](https://raw.githubusercontent.com/ericyishi/img-folder/master/checkInterface03.png)
                                    <p>图3：运行后的结果展示</p>
                   
* `实际结果`是请求返回的数据，也写进了表格里，方便查看分析。 


## 总结 

1. 如何读取excel。使用POI来读写excel单元格的内容(为了方便直接使用`maven`添加相关依赖);
2. 如何判断接口是否通过测试。从可通性以及数据正确性两面来判断的。所以我根据具体项目，添加的期望值应包括返回标志位：errMsg 以及观察点：city 两部分。
3. 比较数据。要把期望值每个单独提取出来（以分号作为分隔，放到Map中），到请求返回的数据中去查找。因为不清楚返回数据的结构是怎么样的，所以需要递归查找，如果期望值所有值都匹配那么就认为是接口正确的。

 


