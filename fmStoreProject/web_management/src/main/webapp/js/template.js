Vue.component('v-select', VueSelect.VueSelect);
new Vue({
    el:"#app",
    data:{
        savee:false,

        tempList:[],
        temp:{},
        searchTemp:{},
        page: 1,  //显示的是哪一页
        pageSize: 10, //每一页显示的数据条数
        total: 150, //记录总数
        maxPage:9,
        selectIds:[], //记录选择了哪些记录的id

        brandsOptions: [], // 所有品牌
        placeholder: '可以进行多选',
        selectBrands: [],
        sel_brand_obj: [], // 选中品牌

        specOptions: [], // 所有规格
        selectSpecs: [],
        sel_spec_obj: [], // 选中规格

        addname:'',

        tid:null,
        selectedID:[]
    },
    methods:{
        pageHandler: function (page) {
            _this = this;
            this.page = page;
            axios.post("/temp/search.do?page="+page+"&pageSize="+this.pageSize,this.searchTemp)
                .then(function (response) {
                    //取服务端响应的结果
                    _this.tempList = response.data.rows;
                    _this.total = response.data.total;
                    console.log(response.data);
                }).catch(function (reason) {
                console.log(reason);
            })
        },

        /*
        * [{"id":4,"name":"小米"},{"id":2,"name":"华为"}]
        *  小米,华为
        * */
        jsonToString:function (jsonStr,key) {
            //1.将传入的字符串转成json
            var jsonObj = JSON.parse(jsonStr);
            var value = "";
            for(var i=0; i<jsonObj.length;i++){
                if (i > 0){
                    value +=",";
                }
                value += jsonObj[i][key];
            }
            return value;
        },

        selected_brand: function(values){
            /*this.selectBrands =values.map(function(obj){
                return obj;
            });
            console.log(this.selectBrands);*/
            console.log(this.sel_brand_obj);
        },

        selected_spec: function(values){
            this.selectSpecs =values.map(function(obj){
                return obj.id
            });
            console.log(this.sel_spec_obj);
        },


        // 获取所有品牌和所有规格
        selLoadData:function () {
            var _this = this;
            axios.get("/brand/selectOptionList.do")
                .then(function (response) {
                  console.log(response.data);
                    _this.brandsOptions = response.data;
                }).catch(function (reason) {
                console.log(reason);
            });
            axios.get("/spec/selectOptionList.do")
                .then(function (response) {
                    console.log(response.data);
                    _this.specOptions = response.data;
                }).catch(function (reason) {
                console.log(reason);
            });
        },


        save:function () {

            var entity = {
                    id:this.tid,
                    name:this.addname,
                    specIds:this.sel_spec_obj,
                    brandIds:this.sel_brand_obj,
            }

            let url = '';
            if (this.savee) {
                url = '/temp/add.do';
            }else{
                url = '/temp/update.do';
            }


            axios.post(url,entity)
                .then(function (response) {
                    console.log(response);
                    _this.pageHandler(1);
                }).catch(function (reason) {
                console.log(reason);
            });
        },

        edit(id){

            this.savee = false;
            _this = this;
            // 查单个记录
            axios.get("/temp/findOne.do?id=" + id)
                .then(function (response) {
                    console.log(response.data);
                    _this.tid = response.data.id;
                    _this.addname = response.data.name;
                    _this.sel_spec_obj = JSON.parse(response.data.specIds);
                    _this.sel_brand_obj = JSON.parse(response.data.brandIds);
                }).catch(function (reason) {
                console.log(reason);
            });
        },

        toNew(){
            this.savee = true;
            this.tid = null;
            this.addname = '';
            this.sel_spec_obj = [];
            this.sel_brand_obj = [];
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
            axios.post("/temp/delete.do",Qs.stringify({idx:this.selectedID},{indices:false})).then(function (response) {
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

    },
    created: function() {
        this.pageHandler(1);
        this.selLoadData();
    }

});