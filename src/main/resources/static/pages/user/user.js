let vue = new Vue({
    el: '#app',
    components:{
        'leasing':httpVueLoader('/pages/leasing/leasing.vue')
    },
    data: function() {
        return {
            visible: false,
            addDialog: false,
            leasingDialog: false,
            url: '/user',
            tableData: [],
            currentPage: 1,
            pageSize: 10,
            total: 0,
            form: {
                name: '',
                sex: '0',
                phone: '',
                age: 21,
                memo: '',
            },
            rules: {
                name: [
                    {required: true, message: '请输入姓名', trigger: 'blur'},
                    {max: 20, message: '长度最多 10 个字符', trigger: 'blur'}
                ],

            },


        }
    },
    methods:{
        link(type){
            let url = "";
            switch(type){
                case "0":
                    url = "/user/list";
                    break;
                case "1":
                    url = "/room/list";
                    break;
                case "2":
                    url = "/leasing/backLog";
                    break;
                default:
                    break;
            }
            location.href = url;
        },
        formatter(row, column) {
            return row.address;
        },
        tableRowClassName({row, rowIndex}) {
            if (rowIndex % 3 === 1) {
                return 'warning-row';
            } else if (rowIndex % 3 === 2) {
                return 'success-row';
            }
            return '';
        },
        handleSizeChange(val) {
            console.log(`每页 ${val} 条`);
            this.pageSize = val;
            this.init();
        },
        handleCurrentChange(val) {
            console.log(`当前页: ${val}`);
            this.currentPage = val;
            this.init();
        },
        submitForm(formName) {
            let self = this;
            this.$refs[formName].validate((valid) => {
                if (valid) {
                    self.add(self.form);
                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        resetForm(formName){
            this.$refs[formName].resetFields();
        },
        add(value){
            let self = this;
            console.log('value',value);
            axios({
                // 默认请求方式为get
                method: 'post',
                url: self.url + "/add",
                // 传递参数
                data: value,
                responseType: 'json'
            }).then(response => {
                // 请求成功
                let res = response.data;
                console.log(res);
                if (res && res.success){
                    this.$message({
                        message: '保存成功',
                        type: 'success',
                        onClose: ()=>{
                            self.init();
                            self.resetForm('form');
                        }
                    });
                    self.addDialog = false;
                }else{
                    this.$message({
                        message: '保存失败',
                        type: 'warning'
                    });
                }
            }).catch(error => {
                // 请求失败，
                console.log(error);
                this.$message({
                    message: '保存出错',
                    type: 'error'
                });
            });
        },
        init(){
            let self = this;
            let page = {
                pageSize: self.pageSize,
                currentPage: self.currentPage,
                total: self.total,
            }
            axios({
                // 默认请求方式为get
                method: 'post',
                url: self.url + "/list",
                // 传递参数
                data: page,
                // params: {
                //     key: value
                // },
                // 设置请求头信息
                // headers: {
                //     key: value
                // },
                responseType: 'json'
            }).then(response => {
                // 请求成功
                let res = response.data;
                if (res && res.data){
                    self.tableData = res.data;
                }
                if (res && res.page){
                    let p = res.page;
                    self.pageSize = p.pageSize;
                    self.currentPage = p.currentPage;
                    self.total = p.total;
                }
                console.log(res);
            }).catch(error => {
                // 请求失败，
                console.log(error);
            });
        },
        formatter(row, column) {
            let res = "";
            if (row.createDate && row.createDate.length > 10){
                res = row.createDate.substring(0,10);
            }
            return res;
        },
        handleEdit(index, row) {
            console.log(index, row);
            this.form = row;
            this.addDialog = true;
        },
        handleHistory(index, row) {
            console.log(index, row);
            this.form = row;
            this.leasingDialog = true;
        },

    },
    mounted(){
        this.init();
    }
})