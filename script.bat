cd %~dp0
@echo 开始打包
gradlew.bat clean assembleProductReleaseChannels -PchannelList=Yhy,yingyongbao,m360,baidu,xiaomi,Jinli,oppo,Huawei,vivo,Samsung,Lenovo,anzhi,wandoujia,sougou,meizu,mumayi,leshi,youyi,weixinguanzhu,weixincaidan,weixindingyuehao,yhyshenzhen,yhynanning,yhyhangzhou,yhyxiamen,yhytianjin,yhyshanghai,yhyguangzhou,yhychengdu,yhyhefei,yhynanjing,yhychongqing,yhywuhan,yhybeijing
pause