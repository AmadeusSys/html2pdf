# html2pdf

快速入门 

项目下载到本地后运行
访问 http://127.0.0.1:8080/contract/previewCodePdf?firstParty=甲方&secondParty=乙方 预览pdf
访问 http://127.0.0.1:8080/contract/downloading?firstParty=甲方&secondParty=乙方 下载pdf
文档中甲方乙方两个变量通过 firstParty 与 secondParty 控制
由于示例中没有使用数据库，直接使用的文件保存，pdf源文件在 document/LaborContract.html 中 

动态存储的html转为pdf导出 盖章等操作

主要设计思路

用户通过富文本编辑器自定义模板的内容与样式

然后使用freemarker对用户的html代码进行赋值

再使用jsoup修复不规范的代码

最后调用iText生成pdf流文件，添加盖章水印，预览或导出。

主要坑

普通HTML代码iText不识别必须要标准xml才行

js自动转义 freemarker 标签 导致标签不能二次编辑

富文本编辑器篡改标签代码导致freemarker标签不能正确放置

iText 不支持中文，封装了一个字体选择的库，
