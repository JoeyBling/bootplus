# apidocjs说明

&emsp;&emsp;官网 ➡️[https://apidocjs.com/](https://apidocjs.com/ "https://apidocjs.com/") 目前使用版本：[0.24.0](https://www.npmjs.com/package/apidoc?activeTab=versions)

## 接口文档发布
- **全局安装 `apidoc`**
```bash
npm install -g apidoc 或者 yarn global add apidoc
```

> 发布命令(生成api文档)`apidoc -c ./apidoc.json -f .java -i ./ -o ./src/main/resources/statics/apidoc/`

## 自定义优化(可选)
> 前言：如果是全局安装找到全局安装目录，不是全局安装目录为`node_modules`，建议使用全局安装

#### 1、自定义页脚
&emsp;&emsp;打开文件`apidoc/template/index.html`，找到87行，替换以下内容

```html

      <div class="content">
        {{__ "Generated with"}} <a target="_blank" href="https://zhousiwei.gitee.io/ibooks/">試毅-思伟</a> @{{{generator.name}}} {{{generator.version}}} - {{{generator.time}}}
      </div>
      <!-- <div class="content">
        {{__ "Generated with"}} <a href="{{{generator.url}}}">{{{generator.name}}}</a> {{{generator.version}}} - {{{generator.time}}}
      </div> -->

```

#### 2、更改默认请求方式为json
&emsp;&emsp;打开文件`apidoc/template/index.html`，找到278行，新增以下内容，并注释
{{#if article.parameter}} 标签内的内容

```html

      <!-- 默认使用json请求 -->
      <div class="{{../id}}-sample-request-param-body {{../id}}-sample-header-content-type-body">
        <!-- hide -->
        <div class="hide">
          <input type="checkbox" data-sample-request-param-group-id="sample-request-param-{{@index}}"  name="{{../id}}-sample-request-param" value="{{@index}}" class="sample-request-param sample-request-switch" checked/>
          <select name="{{../id}}-sample-header-content-type" class="{{../id}}-sample-request-param-select sample-header-content-type sample-header-content-type-switch">
            <option value="body-json" selected>body/json</option>
          </select>
        </div>
        <div class="form-group">
          <div class="input-group">
              <textarea id="sample-request-body-json" class="form-control sample-request-body" data-sample-request-body-group="sample-request-param-{{@./index}}" rows="6" style="OVERFLOW: visible" {{#if optional}}data-sample-request-param-optional="true"{{/if}}>
{{reformat article.parameter.examples.0.content article.parameter.examples.0.type}}</textarea>
            <div class="input-group-addon">json</div>
          </div>
        </div>
      </div>

```


#### 3、请求添加自定义请求头
&emsp;&emsp;打开文件`apidoc/template/utils/send_sample_request.js`，找到149行，替换以下内容

```javascript

        // send AJAX request, catch success or error callback
        try {
            var jsonData = JSON.stringify(JSON.parse(param));
            header["sign"]="test";
            $.ajax({
                url: url,
                dataType: "json",
                contentType: "application/json",
                data: jsonData,
                headers: header,
                xhrFields: {
                    // 携带凭证(为了携带cookie)
                    withCredentials: true
                    // withCredentials: false
                },
                type: type.toUpperCase(),
                success: displaySuccess,
                error: displayError
            });
        } catch(e) {
            var ajaxRequest = {
                url        : url,
                headers    : header,
                data       : param,
                type       : type.toUpperCase(),
                success    : displaySuccess,
                error      : displayError
            };

            if(header['Content-Type'] == 'multipart/form-data'){
                delete ajaxRequest.headers['Content-Type'];
                ajaxRequest.contentType=false;
                ajaxRequest.processData=false;
            }
            $.ajax(ajaxRequest);
        }
```

#### 4、锚链接含有特殊字符替换
&emsp;&emsp;打开文件`apidoc/template/main.js`，找到618行，替换以下内容,并添加自定义解码函数

```javascript

        if(!!id) {
            try {
                id = decodeToJquerySelector(id);
            } catch(ex) {
            }
            if ($(id).length > 0)
                $('html,body').animate({ scrollTop: parseInt($(id).offset().top) }, 0);
        }

    /**
     * 自定义解码字符串
     * @param {string} str 要解码的字符串
     */
    function decodeToJquerySelector(str = ''){
        if(!!str){
            // Some characters, such as / , Space , ( , ) , .
            // 转码(如果jquery要选择的元素id中带有点符号，在选择时需要在点前面加上1个反斜)
            return decodeURIComponent(str.replace(/\//, '\\\/').replace(/\s/g, "-").replace("(", "\\(").replace(")", "\\)"))
                .replace(/\./,'\\.');
        }
    }

```

#### 5、内容滚动导航点击效果失效
&emsp;&emsp;打开文件`apidoc/template/main.js`，找到409行，替换以下内容。
> 解码函数在上面

```javascript
        var id = decodeToJquerySelector($(this).attr('href'));
```


#### 6、字体网站替换CDN访问（待完善）
&emsp;&emsp;打开文件`apidoc/template/vendor/webfontloader.js`，搜索内容`fonts.googleapis.com/css`，如果此网站不可用可替换为其他CDN