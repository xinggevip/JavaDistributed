new Vue({
    el:"#app",
    data:{
        categoryList1:[],//分类1数据列表
        categoryList2:[],//分类2数据列表
        categoryList3:[],//分类3数据列表
        grade:1,  //记录当前级别
        categorySel1ID:-1,
        categorySel2ID:-1,
        categorySel3ID:-1,
        typeId:0,

        brandList:[],
        selectBrandId:-1,
        currngImg:{
            url:'',
            color:''
        },
        imgList:[],

        specList:[],//从服务器获取的所有规格列表,

        specSelItems:[],//当前选择的规格集合,
        itemList:[],
        isEnableSpec:1, //是否启用规格
        goodsEntity:{
            goods:{},
            goodsDesc:{},
            itemList:{}
        }//最终保存商品的实体
    },
    methods:{
        loadCateData: function (id) {
            var _this = this;
            axios.post("/itemCat/findByParentId.do?parentId="+id)
                .then(function (response) {
                    if (_this.grade == 1){
                        //取服务端响应的结果
                        _this.categoryList1 = response.data;
                    }
                    if (_this.grade == 2){
                        //取服务端响应的结果
                        _this.categoryList2 =response.data;
                    }
                    if (_this.grade == 3){
                        //取服务端响应的结果
                        _this.categoryList3 =response.data;
                    }
                }).catch(function (reason) {
                console.log(reason);
            })
        },
        getCategorySel:function (grade) {

            if (grade == 1){

                this.categoryList2 = [];//清空二级分类数据
                this.categorySel2ID=-1;   //默认选择
                this.categoryList3 = []; //清空三级分类数据
                this.categorySel3ID=-1; //默认选择

                this.grade = grade + 1;
                this.loadCateData(this.categorySel1ID);
            }
            if(grade == 2) { //第2级选项改变
                this.categoryList3 = []; //清空三级分类数据
                this.categorySel3ID=-1; //默认选择

                this.grade = grade + 1;// 加载第3级的数据
                this.loadCateData(this.categorySel2ID);
            }
            if (grade == 3){ //第3级选项改变
                //加载模板
                var _this = this;
                axios.post("/itemCat/findOneCategory.do?id="+this.categorySel3ID)
                    .then(function (response) {
                        _this.typeId  = response.data.typeId;
                    }).catch(function (reason) {
                    console.log(reason);
                })

            }

        },
        uploadFile:function () {
           var formData =  new FormData();
           formData.append("file",file.files[0])

            var instance = axios.create({
               withCredentials:true
            });

           var _this =this;
            instance.post("/upload/uploadFile.do",formData).then(function (response) {
                if (response.data.success){
                    console.log(response.data.message);
                    _this.currngImg.url = response.data.message;
                }
            }).catch(function (reason) {
                    console.log(reason);
            });
        },
        saveImage:function () {
            if (this.currngImg.color == '' || this.currngImg.url == ''){
                alert("请输入颜色或上传图片");
                return;
            }
            this.imgList.push({color:this.currngImg.color,url:this.currngImg.url});
            this.currngImg.color='';
            this.currngImg.url='';
        },
        delImg:function (url,index) {
            var _this = this;
            axios.get("/upload/delImg.do?url="+url)
                .then(function (response) {
                    console.log(response.data);
                    if (response.data.success){
                        // 从数组中移除
                        _this.imgList.splice(index,1);
                    } else {
                        alert("删除失败");
                    }

                }).catch(function (reason) {
                console.log(reason);
            });
        },
        //从指定集合当中查询某一个值是否已经存在
        //如果存在 把存在值返回
        searchObjectWithKey:function(list,key,value){
            for(var i=0; i<list.length;i++){
                if (list[i][key] == value){
                    return list[i];
                }
            }
            return null;
        },
        //[{"specName":"选择颜色",specOption:[]}]
        updateSpecStatus:function (event,specName,specOption) {
            var obj = this.searchObjectWithKey(this.specSelItems,"specName",specName);
            if (obj != null){
                if (event.target.checked){ //选中
                    obj.specOptions.push(specOption);
                } else { //取消选中
                   var idx = obj.specOptions.indexOf(specOption);
                    obj.specOptions.splice(idx,1);
                    if (obj.specOptions.length == 0){
                        var idx = this.specSelItems.indexOf(obj);
                        this.specSelItems.splice(idx,1);
                    }
                }
            } else {
                this.specSelItems.push({"specName":specName,"specOptions":[specOption]});
            }

            console.log(this.specSelItems);
            this.createItem();
        },
        createItem:function () {

            var rowList = [{spec:{},price:0,num:9999,status:'0',isDefault:'0'}];

            for (var i = 0; i<this.specSelItems.length;i++){
                var  spec =  this.specSelItems[i];
                var specName =spec.specName;
                var specOption = spec.specOptions;
                var newRowList = [];
                for (var j=0; j < rowList.length; j++){
                    var oldRow = rowList[j];
                    for (var k = 0; k< specOption.length; k++){
                        var newRow = JSON.parse(JSON.stringify(oldRow));
                         newRow.spec[specName]=specOption[k];
                         newRowList.push(newRow);
                    }
                }
                rowList = newRowList;
            }
            this.itemList = rowList;
            console.log(this.itemList);
        },
        saveGoods:function () {

            this.goodsEntity.goods.category1Id = this.categorySel1ID;
            this.goodsEntity.goods.category2Id = this.categorySel2ID;
            this.goodsEntity.goods.category3Id = this.categorySel3ID;

            this.goodsEntity.goods.typeTemplateId=this.typeId,
            this.goodsEntity.goods.brandId=this.selectBrandId,
            this.goodsEntity.goods.isEnableSpec=this.isEnableSpec,

            this.goodsEntity.goodsDesc.itemImages=this.imgList,
            this.goodsEntity.goodsDesc.specificationItems=this.specSelItems,
            this.goodsEntity.goodsDesc.introduction=UE.getEditor('editor').getContent();

            this.goodsEntity.itemList = this.itemList;

            var id = this.GetQueryString("id");
            var url = '';
            if (id != null){
                url = '/goods/update.do';
            }else {
                url = '/goods/add.do';
            }
            //发送请求
            axios.post(url,this.goodsEntity)
                .then(function (response) {
                    console.log(response.data);
                    location.href="goods.html";
                }).catch(function (reason) {
                alert(response.data.message);
            });
        },
        GetQueryString:function(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
            var r = window.location.search.substr(1).match(reg);
            if (r!=null) return (r[2]); return null;
        },
        checkAttributeValue:function(specName,optionName){ //判断当前规格是否为选中状态
            var items = this.specSelItems;
            var object = this.searchObjectWithKey(items,"specName",specName);
            if(object != null){
                if(object.specOptions.indexOf(optionName)>=0){
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        }
    },
    watch:{
        typeId:function (newValue,oldValue) {

            var _this = this;
            _this.brandList =[];
            _this.selectBrandId = -1;
            //加载品牌
            axios.post("/temp/findOne.do?id="+newValue)
                .then(function (response) {
                    console.log(response.data);
                    _this.brandList = JSON.parse(response.data.brandIds);
                    if (_this.goodsEntity.goods.brandId != null){
                        _this.selectBrandId = _this.goodsEntity.goods.brandId;
                    }
                }).catch(function (reason) {
                console.log(reason);
            });

            _this.specList = [];
            //加载规格数据
            axios.get("/temp/findBySpecList.do?id="+newValue)
                .then(function (response) {
                    console.log(response.data);
                    _this.specList = response.data;
                }).catch(function (reason) {
                console.log(reason);
            });

        }

    },
    created: function() {
        this.loadCateData(0);
    },
    mounted:function () {
        var id = this.GetQueryString("id");
        var _this = this;
        if (id != null){
            //根据id查询当前商品
            axios.get("/goods/findOne.do?id="+id)
                .then(function (response) {
                    console.log(response.data);
                    var goodsEntity = response.data;
                    _this.goodsEntity.goods = goodsEntity.goods;
                    _this.goodsEntity.goodsDesc = goodsEntity.goodsDesc;
                    _this.categorySel1ID = goodsEntity.goods.category1Id ;
                    _this.categorySel2ID = goodsEntity.goods.category2Id ;
                    _this.categorySel3ID = goodsEntity.goods.category3Id ;

                    _this.typeId = goodsEntity.goods.typeTemplateId
                    _this.selectBrandId = goodsEntity.goods.brandId
                    _this.isEnableSpec = goodsEntity.goods.isEnableSpec,

                    //回显富文本
                    UE.getEditor("editor").ready(function () {
                        UE.getEditor("editor").setContent(goodsEntity.goodsDesc.introduction);
                    });

                    _this.imgList=JSON.parse(goodsEntity.goodsDesc.itemImages);
                    _this.specSelItems=JSON.parse(goodsEntity.goodsDesc.specificationItems);

                    _this.itemList = goodsEntity.itemList;
                    //库存列表
                    for (var i = 0; i< _this.itemList.length; i++){
                        _this.itemList[i].spec =  JSON.parse(_this.itemList[i].spec);
                    }
                    //控制选项默认选中状态
                    if (_this.categorySel1ID >= 0){
                        _this.grade  = 2;
                        //加载第二级的列表
                        _this.loadCateData(_this.categorySel1ID);
                        _this.categorySel2ID = goodsEntity.goods.category2Id ;

                        if (_this.categorySel2ID >= 0){
                            axios.post("/itemCat/findByParentId.do?parentId="+_this.categorySel2ID)
                                .then(function (response) {
                                 _this.categoryList3 =response.data;
                                }).catch(function (reason) {
                                console.log(reason);
                            });
                            _this.categorySel3ID = goodsEntity.goods.category3Id;
                        }
                    }

                }).catch(function (reason) {
                console.log(reason);
            });
        }
    }
});
