new Vue({
    el:"#app",
    data:{
        specList:[],
        spec:{},//规格
        page: 1,  //显示的是哪一页 当前角标
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPage:9,
        selectedID:[],
        searchObj:{},
        specOption:[], //规格选项
        selectIds:[]
    },
    methods:{
        pageHandler:function (page) {
            this.page = page;
            var _this =this;
            axios.post("/spec/findPage.do?page="+this.page+"&pageSize="+this.pageSize,this.searchObj).then(function (response) {
                _this.specList = response.data.rows;
                _this.total = response.data.total;
                console.log(_this.specList);
            }).catch(function (reason) {
                console.log(reason);
            });
        },
        addRow:function () {
            this.specOption.push({});
        },
        deleteSpecOption:function (index) {
            this.specOption.splice(index,1);
        },
        saveSpec:function () {
                var _this = this;
                var specEntity = {
                    specification:this.spec,
                    specOptionList:this.specOption
                };
                var url = '';
                if (this.spec.id != null){
                    url='/spec/update.do';
                } else {
                    url ='/spec/add.do'
                }
                axios.post(url,specEntity)
                    .then(function (response) {
                        //刷新页面
                        _this.pageHandler(_this.page);
                        _this.specOption=[];
                        _this.spec={};
                    }).catch(function (reason) {
                    console.log(reason);
                });

        },

        findOne:function (id) {
            _this = this;
            axios.get("/spec/findOne.do",{params:{id:id}})
                .then(function (response) {
                    _this.spec = response.data.specification;
                    _this.specOption = response.data.specOptionList;
                }).catch(function (reason) {
                console.log(reason);
            });
        },
        deleteSelection:function (event,id) {
            // 复选框选中
            if(event.target.checked){
                // 向数组中添加元素
                this.selectIds.push(id);
            }else{
                // 从数组中移除
                var idx = this.selectIds.indexOf(id);
                this.selectIds.splice(idx,1);
            }
        },
        deleteSpec:function () {
            var _this = this;
            //使用qs插件 处理数组
            axios.post('/spec/delete.do',Qs.stringify({ids: _this.selectIds},{ indices: false }))
                .then(function (response) {
                    _this.pageHandler(_this.page);
                    _this.selectIds = [];
                }).catch(function (reason) {
                console.log(reason);
            })
        }
    },
    created:function () {
        this.pageHandler(1);
    }

});