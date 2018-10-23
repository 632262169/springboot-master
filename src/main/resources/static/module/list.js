// 对应table标签的id
$("#indexTable").bootstrapTable({
	url : "/user/getPers", // AJAX获取表格数据的url
	striped : true, // 是否显示行间隔色(斑马线)
	pagination : true, // 是否显示分页（*）
	//singleSelect : true, // 单选checkbox
	sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
	paginationLoop : false, // 当前页是边界时是否可以继续按
	queryParams : function(params) { // 请求服务器数据时发送的参数，可以在这里添加额外的查询参数，返回false则终止请求
		return {
			limit : params.limit, // 每页要显示的数据条数
			offset : params.offset, // 每页显示数据的开始行号
		// sort: params.sort, // 要排序的字段
		// sortOrder: params.order, // 排序规则
		// dataId: $("#dataId").val() // 额外添加的参数
		}
	},// 传递参数（*）
	pageNumber : 1, // 初始化加载第一页，默认第一页
	pageSize : 10, // 每页的记录行数（*）
	pageList : [ 10, 25, 50, 100, 'all' ], // 可供选择的每页的行数（*）
	contentType : "application/x-www-form-urlencoded",// 一种编码。在post请求的时候需要用到。这里用的get请求，注释掉这句话也能拿到数据
	// search: true, //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
	strictSearch : false, // 是否全局匹配,false模糊匹配
	showColumns : true, // 是否显示所有的列
	showRefresh : true, // 是否显示刷新按钮
	minimumCountColumns : 2, // 最少允许的列数
	clickToSelect : false, // 是否启用点击选中行
	// height: 500, //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
	// uniqueId: "id", //每一行的唯一标识，一般为主键列
	showToggle : true, // 是否显示详细视图和列表视图的切换按钮
	cardView : false, // 是否显示详细视图
	detailView : false, // 是否显示父子表
	cache : false, // 设置为 false 禁用 AJAX 数据缓存， 默认为true
	sortable : true, // 是否启用排序
	sortOrder : "asc", // 排序方式
	sortName : 'sn', // 要排序的字段
	columns : [
//	{
//		checkbox : true
//	}, 
	{
		field : 'sn', // 返回json数据中的name
		title : '订货号', // 表格表头显示文字
		align : 'center', // 左右居中
		valign : 'middle' // 上下居中
	}, {
		field : 'productname',
		title : '商品名称',
		align : 'center',
		valign : 'middle'
	}, {
		field : 'cost',
		title : '价格(¥)',
		align : 'center',
		valign : 'middle',
		sortable : true
	}, {
		field : 'brankname',
		title : '品牌',
		align : 'center',
		valign : 'middle',
	}, {
		field : 'specificationvalues',
		title : '规格',
		align : 'center',
		valign : 'middle',
	}, {
		field : 'areaname',
		title : '产地',
		align : 'center',
		valign : 'middle',
	} ],
	onLoadSuccess : function() { // 加载成功时执行
		console.info("加载成功");
	},
	onLoadError : function() { // 加载失败时执行
		console.info("加载数据失败");
	}

});