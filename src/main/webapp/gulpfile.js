var gulp = require('gulp'),//全局安装cnpm install gulp -g ；项目内安装 cnpm install gulp --save-dev
	rev = require('gulp-rev'),//静态资源内容，生成md5签名，打包出来的文件名会加上md5签名，同时生成一个json用来保存文件名路径对应关系。 cnpm install --save-dev gulp-rev
	revCollector = require('gulp-rev-collector'),//从manifests中获取静态资源版本数据, 并且替换相应文件中的链接 cnpm install --save-dev gulp-rev-collector
	runSequence = require('run-sequence');//控制多个任务进行顺序执行或者并行执行  cnpm install run-sequence --save-dev

/**
 * ============================pc================================
 * */
//iconfont & img 更改版本号，并通过rev.manifest将对应的版本号用json表示出来
gulp.task('font', function(){
	return gulp.src('assets/pc/iconfont/*')
		.pipe(rev())//set hash key
		.pipe(rev.manifest())//set hash key json
		.pipe(gulp.dest('rev/pc/iconfont'));
});
gulp.task('img', function(){
	return gulp.src('assets/pc/img/*')
		.pipe(rev())
		.pipe(rev.manifest())
		.pipe(gulp.dest('rev/pc/img'));
});

//通过hash来精确定位到css中需要更改的部分,然后将修改成功的文件生成到指定目录
gulp.task('revCss',function(){
	return gulp.src(['rev/pc/**/*.json','assets/pc/css/**/*.{css,less}'])
		.pipe( revCollector() )
		.pipe(gulp.dest('assets/pc/css/'));
});

//css文件，更改版本号，并通过rev.manifest将对应的版本号用json表示出来
gulp.task('css',function(){
	return gulp.src('assets/pc/css/**/*.css')
		.pipe(rev())
		.pipe(rev.manifest())
		.pipe(gulp.dest('rev/pc/css'))
});

//js文件，更改版本号，并通过rev.manifest将对应的版本号用json表示出
gulp.task('js',function(){
	return gulp.src('assets/pc/js/**/*.js')
		.pipe(rev())
		.pipe(rev.manifest())
		.pipe(gulp.dest('rev/pc/js'))
});


//通过hash来精确定位到html模板中需要更改的部分,然后将修改成功的文件生成到指定目录
gulp.task('revHtml',function(){
	return gulp.src(['rev/pc/**/*.json','pages/pc/**/*.html'])
		.pipe( revCollector() )
		.pipe(gulp.dest('pages/pc/'));
});

//开发构建
gulp.task('dev', function (done) {
	condition = false;
	runSequence(
		['font', 'img'],
		['revCss'],
		['css'],
		['js'],
		['revHtml'],
		done);
});

/**
 * ============================wechat================================
 * */
//iconfont & img 更改版本号，并通过rev.manifest将对应的版本号用json表示出来
gulp.task('wx_font', function(){
	return gulp.src('assets/wechat/iconfont/*')
		.pipe(rev())//set hash key
		.pipe(rev.manifest())//set hash key json
		.pipe(gulp.dest('rev/wechat/iconfont'));
});
gulp.task('wx_img', function(){
	return gulp.src('assets/wechat/images/*')
		.pipe(rev())
		.pipe(rev.manifest())
		.pipe(gulp.dest('rev/wechat/images'));
});

//通过hash来精确定位到css中需要更改的部分,然后将修改成功的文件生成到指定目录
gulp.task('wx_revCss',function(){
	return gulp.src(['rev/wechat/**/*.json','assets/wechat/css/**/*.{css,less}'])
		.pipe( revCollector() )
		.pipe(gulp.dest('assets/wechat/css/'));
});

//css文件，更改版本号，并通过rev.manifest将对应的版本号用json表示出来
gulp.task('wx_css',function(){
	return gulp.src('assets/wechat/css/**/*.css')
		.pipe(rev())
		.pipe(rev.manifest())
		.pipe(gulp.dest('rev/wechat/css'))
});

//js文件，更改版本号，并通过rev.manifest将对应的版本号用json表示出
gulp.task('wx_js',function(){
	return gulp.src('assets/wechat/js/**/*.js')
		.pipe(rev())
		.pipe(rev.manifest())
		.pipe(gulp.dest('rev/wechat/js'))
});


//通过hash来精确定位到html模板中需要更改的部分,然后将修改成功的文件生成到指定目录
gulp.task('wx_revHtml',function(){
	return gulp.src(['rev/wechat/**/*.json','pages/wechat/**/*.html'])
		.pipe( revCollector() )
		.pipe(gulp.dest('pages/wechat/'));
});

//微信端构建
gulp.task('wx_dev', function (done) {
	condition = false;
	runSequence(
		['wx_font', 'wx_img'],
		['wx_revCss'],
		['wx_css'],
		['wx_js'],
		['wx_revHtml'],
		done);
});

/**
 * ============================patient================================
 * */
//iconfont & img 更改版本号，并通过rev.manifest将对应的版本号用json表示出来
gulp.task('pt_font', function(){
	return gulp.src('assets/patient/iconfont/*')
		.pipe(rev())//set hash key
		.pipe(rev.manifest())//set hash key json
		.pipe(gulp.dest('rev/patient/iconfont'));
});
gulp.task('pt_img', function(){
	return gulp.src('assets/patient/img/*')
		.pipe(rev())
		.pipe(rev.manifest())
		.pipe(gulp.dest('rev/patient/img'));
});

//通过hash来精确定位到css中需要更改的部分,然后将修改成功的文件生成到指定目录
gulp.task('pt_revCss',function(){
	return gulp.src(['rev/patient/**/*.json','assets/patient/css/**/*.{css,less}'])
		.pipe( revCollector() )
		.pipe(gulp.dest('assets/patient/css/'));
});

//css文件，更改版本号，并通过rev.manifest将对应的版本号用json表示出来
gulp.task('pt_css',function(){
	return gulp.src('assets/patient/css/**/*.css')
		.pipe(rev())
		.pipe(rev.manifest())
		.pipe(gulp.dest('rev/patient/css'))
});

//js文件，更改版本号，并通过rev.manifest将对应的版本号用json表示出
gulp.task('pt_js',function(){
	return gulp.src('assets/patient/js/**/*.js')
		.pipe(rev())
		.pipe(rev.manifest())
		.pipe(gulp.dest('rev/patient/js'))
});


//通过hash来精确定位到html模板中需要更改的部分,然后将修改成功的文件生成到指定目录
gulp.task('pt_revHtml',function(){
	return gulp.src(['rev/patient/**/*.json','pages/patient/**/*.html'])
		.pipe( revCollector() )
		.pipe(gulp.dest('pages/patient/'));
});

//微信端构建
gulp.task('pt_dev', function (done) {
	condition = false;
	runSequence(
		['pt_font', 'pt_img'],
		['pt_revCss'],
		['pt_css'],
		['pt_js'],
		['pt_revHtml'],
		done);
});

/*==三端一起构建==========================================================*/
gulp.task('all_dev', function (done) {
	condition = false;
	runSequence(
		['dev','wx_dev','pt_dev'],
		done);
});

gulp.task('default', ['all_dev']);