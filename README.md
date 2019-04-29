## 无侵入式插件换肤

### 使用方式：

1. 在Application中进行初始化
```
SkinManager.init(this);
``` 
2. BaseActivity中进行全局换肤
```
SkinManager.getInstance().updateSkin(this);
```
3. 换肤开启与关闭
```
// 开启换肤
SkinManager.getInstance().loadSkin("/sdcard/skin.skin");
// 还原默认
SkinManager.getInstance().loadSkin(null);
```

### skin插件的生成可以参考skin Module
