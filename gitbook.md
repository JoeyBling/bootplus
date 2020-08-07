# gitbook说明

&emsp;&emsp;GitHub ➡️[https://github.com/GitbookIO/gitbook-cli](https://github.com/GitbookIO/gitbook-cli) 目前使用版本：[3.2.3](https://www.npmjs.com/package/gitbook?activeTab=versions)

- [GitBook的安装、卸载、常见问题](https://www.jianshu.com/p/1f78d8018ea7)
- [GitBook相关配置及优化](https://www.jianshu.com/p/53fccf623f1c)
- [GitBook生成pdf出现中文不显示或乱码](https://www.jianshu.com/p/e08eb95f79c7)

## 写在前面
- **全局安装 `gitbook`**
```bash
npm install -g gitbook 或者 yarn global add gitbook
npm install -g gitbook-cli
```

> 发布命令(生成文档)`gitbook serve` & 生成打包`gitbook build`

## 自定义优化(可选)
> 前言：建议使用全局安装

#### 1、修改GitBook默认主题报错
&emsp;&emsp;在使用该主题的过程中，发现经常会在控制台报下面的错误，没有找到是哪里的原因，官方也一直没有修复。
> theme.js:4 Uncaught TypeError: Cannot read property 'split' of undefined


&emsp;&emsp;转到用户目录并打开文件`.gitbook/versions/3.2.3/node_modules/gitbook-plugin-theme-default/src/js/theme/navigation.js`，找到`getChapterHash`函数，替换以下内容

```javascript
    //var $link = $chapter.children('a'),
    //    hash = $link.attr('href').split('#')[1];

    //if (hash) hash = '#'+hash;
    //return (!!hash)? hash : '';
    var $link = $chapter.children('a'),
        hash,
        href,
        parts;

    if ($link.length) {
        href = $link.attr('href')
        if (href) {
            parts = href.split('#');
            if (parts.length>1) {
                hash = parts[1];
            }
        }
    }

    if (hash) hash = '#'+hash;
    return (!!hash)? hash : '';
```
&emsp;&emsp;回到 **gitbook-plugin-theme-default** 文件夹，运行 `npm install` 重新编译文件。

#### 2、修改theme-default默认主题样式

&emsp;&emsp;转到用户目录并打开文件夹`.gitbook/versions/3.2.3/node_modules/gitbook-plugin-theme-default/_layouts/website`

1. 修改`favicon.ico` 打开文件`layout.html`注释掉第九行和第十行 `<!-- -->`

2. 修改`Published with GitBook` 打开文件`summary.html`
> 搜索 `https://www.gitbook.com`  并更改成你的博客链接和标题
```html
        <a href="https://zhousiwei.gitee.io" creator="https://github.com/JoeyBling" target="_blank" class="gitbook-link">
           Published with 試毅-思伟<!-- {{ "GITBOOK_LINK"|t }}{{t}} -->
        </a>
```

#### 3、修改插件prism警告
&emsp;&emsp;每次重新执行npm install 后都会出现
```
# warn: "options" property is deprecated, use config.get(key) instead 
# warn: "options.generator" property is deprecated, use "output.name" instead
```

&emsp;&emsp;打开文件`./node_modules/gitbook-plugin-prism/index.js`，更改41行`if (book.options && book.options.generator)`，替换以下内容

```
  if (/^2+\.\d+\.\d+$/.test(book.gitbook.version))
```

#### 4、自定义插件tbfed-pagefooter页脚
&emsp;&emsp;打开文件`./node_modules/gitbook-plugin-tbfed-pagefooter/index.js`，更改成你的博客链接和标题

```
powered by 試毅-思伟
```

#### 5、修改插件百度统计
&emsp;&emsp;打开文件`./node_modules/gitbook-plugin-baidu/book/plugin.js`，替换以下内容
```javascript
require(["gitbook"], function (gitbook) {
    gitbook.events.bind("start", function (e, config) {
        config.baidu = config.baidu || {};
        var _hmt = _hmt || [];
        var hm = document.createElement("script");
        hm.src = "https://hm.baidu.com/hm.js?" + config.baidu.token;
        var s = document.getElementsByTagName("script")[0];
        s.parentNode.insertBefore(hm, s);
    });
});
```

#### 6、修改插件mygitalk使用本地资源
&emsp;&emsp;打开文件`./node_modules/gitbook-plugin-mygitalk/index.js`，替换以下内容并执行命令复制本地资源
```cp -rp ./gitalk/* ./node_modules/gitbook-plugin-mygitalk/assets/```
```
        css: [
            // "https://unpkg.com/gitalk/dist/gitalk.css",
            "gitalk.css",
            "mygitalk.css"
        ],
        js: [
            // "https://unpkg.com/gitalk/dist/gitalk.min.js",
            "gitalk.min.js",
            "mygitalk.js"
        ]
```

## 错误解决
&emsp;&emsp;由于gitbook官方早就弃用`gitbook-cli`了，所以出现Bug大部分都是自己修改源码。
如果可以，可能需要fork `gitbook-cli`来维护它
> 官方原文：https://github.com/GitbookIO/gitbook#%EF%B8%8F-deprecation-warning

1. node高版本问题（目前大概问题是出现在node`12.18.3`问题之后）
报错原文参考：
```
E:\nodejs\node_global\node_modules\gitbook-cli\node_modules\npm\node_modules\graceful-fs\polyfills.js:287
      if (cb) cb.apply(this, arguments)
                 ^
TypeError: cb.apply is not a function
    at E:\nodejs\node_global\node_modules\gitbook-cli\node_modules\npm\node_modules\graceful-fs\polyfills.js:287:18
    at FSReqCallback.oncomplete (fs.js:177:5)
```
问题解决请参考我的回复：
> 回复原文：https://github.com/GitbookIO/gitbook-cli/issues/110#issuecomment-669640662

- if you run `gitbook serve` has error and your `gitbook-cli` is installed globally.
Find the NPM global installation directory and into dir `node_modules/gitbook-cli/node_modules/npm/node_modules` Execute the order
> npm install graceful-fs@latest --save

- if you run `gitbook install` has error
Go to user directory and into dir `.gitbook/versions/最新使用的版本号/node_modules/npm` Execute the order
> npm install graceful-fs@latest --save