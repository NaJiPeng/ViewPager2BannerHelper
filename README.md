# ViewPager2BannerHelper
ViewPager2实现轮播效果的辅助工具，包括：

1. adapter：`LoopAdapter / LoopFragmentAdapter`；
2. controller：`AutoPlayController`；
3. indicator：待实现；
4. transformer：待实现；

## 使用方法：

1. 将以下代码添加到工程的gradle文件中:

   ```groovy
   allprojects {
   		repositories {
   			...
   			maven { url 'https://jitpack.io' }
   		}
   }
   ```

2. 添加依赖

   ```groovy
   dependencies {
   	        implementation 'com.github.NaJiPeng:ViewPager2BannerHelper:0.2.0'
   }
   ```

3. 在工程中使用`ViewPager2`

4. 编写你的`MyAdapter`继承自`LoopAdapter/LoopFragmentAdapter`，并为你的`ViewPager2`设置`MyAdapter`以实现无限轮播

5. 创建并配置`AutoPlayController`并关联你的`ViewPager2`，以实现自动播放