[#-- 看板娘Live2d(https://github.com/JoeyBling/live2d-widget.js) --]
<!-- TODO : 暂未完善 -->
<div id="live2d-widget" class="live2d-widget-container"
     style="position:fixed;right:65px;bottom:0px;width:135px;height:300px;z-index:99999;opacity:0.8;pointer-events:none;">
    <canvas
            id="live2d_canvas"
            style="position:absolute;left:0px;top:0px;width:135px;height:300px;"
            width="135"
            height="300"
            class="live2d_canvas"
    />
</div>
<script type="text/javascript" src="${rc.contextPath}/statics/live2d/live2d.js"></script>
<script type="text/javascript">
    window.loadlive2d && window.loadlive2d("live2d_canvas",
        "${rc.contextPath}/statics/live2d/${live2dModelName!'hibiki'}/assets/${live2dModelName!'hibiki'}.model.json");
</script>