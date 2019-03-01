# zhihudaily
* 实现了知乎日报的主要功能(并且去掉了认为不好看的控件)

![main](https://github.com/931717954/zhihudaily/blob/master/%E4%B8%BB%E9%A1%B5.PNG)

![night](https://github.com/931717954/zhihudaily/blob/master/%E5%A4%9C%E9%97%B4%E4%B8%BB%E9%A1%B5.PNG)

* 完成了下拉刷新 上拉加载等功能
  使用了swiprefreshlayout完成下拉刷新功能 
  设置了一个在recyclerview中设置了footerview，当加载到footerview时获取下一页数据
  
* 文字缓存到运存和内存中，图片使用glide加载
* 使用webview加载由json拼接得到的html

![web](https://github.com/931717954/zhihudaily/blob/master/%E7%BD%91%E9%A1%B5.PNG)
* 完成了夜间模式切换设置
  设置了一套夜间模式使用的颜色值
  
* 完成了重构，代码比之前合理一些，封装了加载信息和jsontool两个类
  当加载信息类除最新资讯外会首先检查请求的文件本地是否已缓存，本地缓存了文件则直接读取本地文件，本地为缓存则请求网络文件，并添加入本地缓存
  
* 使用okhttp以及glide两个框架
