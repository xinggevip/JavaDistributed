new Vue({
    el:"#app",
    data:{
        brandList:[],
        brand:{},
        page: 1,  //显示的是哪一页 当前角标
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPage:9
    },
    methods:{
        pageHandler:function (page) {
            this.page = page;
            var _this =this;
            axios.get("/brand/findPage.do",{params:{page:this.page,pageSize:this.pageSize}}).then(function (response) {
                _this.brandList = response.data.rows;
                _this.total = response.data.total;
            }).catch(function (reason) {
                console.log(reason);
            });
        },
        save:function () {
            var _this = this;
            axios.post("/brand/add.do", _this.brand)
                .then(function (response) {
                    if (response.data.success){
                        console.log(value.data);
                        alert(response.data.message);
                        //刷新页面
                        _this.pageHandler(1);
                        _this.brand = {};
                    } else {
                        alert(response.data.message);
                    }

                })
        }
    },
    created:function () {
        this.pageHandler(1);
    }

});