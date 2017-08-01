# html2pdf
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