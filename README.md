# ViewPager2BannerHelper
ViewPager2实现轮播效果的辅助工具，包括：

1. 无限轮播Adapter：`LoopAdapter / LoopFragmentAdapter`；
2. 自动播放Controller：`AutoPlayController`；
3. 可定制Indicator：`DrawableIndicator`；
4. transformer：待实现；

以上组件均可单独使用，亦可组合使用。

## 使用方法：

### 使用前准备：

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
       implementation 'com.github.NaJiPeng:ViewPager2BannerHelper:0.3.0'
   }
   ```

3. 在项目中引入`ViewPager2`：

   ```groovy
   implementation "androidx.viewpager2:viewpager2:1.0.0"
   ```

   ```xml
   <androidx.viewpager2.widget.ViewPager2
   	android:id="@+id/banner"
   	android:layout_width="match_parent"
   	android:layout_height="match_parent" />
   ```

   ```kotlin
   val banner = findViewById<ViewPager2>(R.id.banner)
   ```

### 使用`LoopAdapter/LoopFragmentAdapter`实现无限轮播效果：

1. 创建自己的Adapter继承自`LoopAdapter/LoopFragmentAdapter`:

   ```kotlin
   class MyAdapter(private val mData: List<String>) : LoopAdapter<MyAdapter.ViewHolder>() {
   
       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
           return ViewHolder(ImageView(parent.context).apply {
               layoutParams = ViewGroup.LayoutParams(
                   ViewGroup.LayoutParams.MATCH_PARENT,
                   ViewGroup.LayoutParams.MATCH_PARENT
               )
               scaleType = ImageView.ScaleType.CENTER_CROP
           })
       }
   
       override fun getRealItemCount(): Int {
           return mData.size
       }
   
       override fun onBindRealViewHolder(holder: ViewHolder, realPosition: Int) {
           Glide.with(holder.imageView)
               .load(mData[realPosition])
               .into(holder.imageView)
       }
   
       class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
           val imageView = itemView as ImageView
       }
   }
   ```

2. 为你的`ViewPager2`设置Adapter：

   ```kotlin
   val myAdapter = MyAdapter(
               listOf(
                   "https://th.wallhaven.cc/lg/vg/vg3wm5.jpg",
                   "https://th.wallhaven.cc/lg/5w/5w6j85.jpg",
                   "https://th.wallhaven.cc/lg/5w/5w6319.jpg",
                   "https://th.wallhaven.cc/lg/ym/ymwggg.jpg"
               )
           )
   banner.adapter = myAdapter
   ```

### 使用`AutoPlayController`实现自动轮播效果：

1. 创建并配置`AutoPlayController`：

   ```kotlin
   AutoPlayController()
   	.enableAutoPlay(true)
   	.setAutoPlayMode(AutoPlayMode.VISIBLE)
   	.setInterval(2000)
   	.setLifecycleOwner(this)
   	.setViewPager2(banner)
   ```

### 使用`DrawableIndicator`实现指示器效果：

1. 在xml视图文件中引入`DrawableIndicator`：

   ```xml
   <com.njp.library.indicator.DrawableIndicator
   	android:id="@+id/indicator"
   	android:layout_width="match_parent"
   	android:layout_height="wrap_content"
   	android:layout_alignParentBottom="true"
   	android:layout_marginBottom="5dp"
   	app:active_item_drawable="@drawable/ic_love"
   	app:active_item_height="13dp"
   	app:active_item_width="13dp" />
   ```

2. 配置indicator：

   ```kotlin
   val indicator = findViewById<DrawableIndicator>(R.id.indicator)
   indicator.setupWithViewPager2(banner)
   ```

