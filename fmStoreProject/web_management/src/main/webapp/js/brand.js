new Vue({
    el:"#app",
    data:{
        brandList:[],
        brand:{
            id:null,
            name:'',
            firstChar:''
        },
        page: 1,  //显示的是哪一页 当前角标
        pageSize: 10, //每一页显示的数据条数
        total: 0, //记录总数
        maxPage:9,
        selectedID:[],
        searchObj:{}
    },
    created(){
        this.pageHandler(1);
    },
    methods:{
        // pageNum发生变化
        pageHandler(page) {
            this.page = page;
            var _this = this;
            _this = this;
            //here you can do custom state update
            this.page = page;
            axios.get('/brand/findPage.do',{params:{page:page,rows:this.pageSize}})
                .then(function (response) {
                    //取服务端响应的结果
                    _this.brandList = response.data.rows;
                    _this.total = response.data.total;
                    console.log(response);
                }).catch(function (reason) {
                console.log(reason);
            })



        },
        // 新增
        add(){
            // alert("更改brand的值");
            this.brand.id = null;
            this.brand.name = '';
            this.brand.firstChar = '';
        },
        // 保存和更新
        save(){

            let url = '';

            if(this.brand.id == null){
                url = '/brand/add.do';
            }else{
                url = '/brand/updata.do';
            }

            console.log(this.brand);
            let _this = this;
            axios.post(url,this.brand)
                .then(function (response) {
                    if (response.data.success){
                        alert(response.data.message);
                        _this.pageHandler(1);
                    }else{
                        alert(response.data.message);
                    }
                }).catch(function (reason) {
                console.log(reason);
            })

        },
        // 根据id查找
        findById(id){
            console.log(id);
            let _this = this;
            axios.get('/brand/findOne.do',{params:{id:id}}).then(function (response) {
                    _this.brand = response.data;
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        // 删除选中多个
        deleteSelection(event,id){
            if(event.target.checked){
                // 向数组中添加元素
                this.selectedID.push(id);
            }else{
                // 从数组中移除
                var idx = this.selectedID.indexOf(id);
                this.selectedID.splice(idx,1);
            }
            console.log(this.selectedID);
        },
        // 删除选中的品牌
        deleteBrand(){
            _this = this;
            axios.post("/brand/delete.do",Qs.stringify({idx:this.selectedID},{indices:false})).then(function (response) {
                    if (response.data.success){
                        alert(response.data.message);
                        _this.pageHandler(1);
                    }else{
                        alert(response.data.message);
                    }
                }).catch(function (reason) {
                console.log(reason);
            })
        },

        // 更新
        updata(item){
            // this.brand = item;

        }


    }
})
