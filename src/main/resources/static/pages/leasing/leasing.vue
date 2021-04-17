<template>
  <div>
    <el-dialog
        title="租赁记录"
        :visible.sync="show"
        width="75%">
      <el-row>
        <el-form ref="form" :inline="true" :model="queryForm" :rules="queryRules" label-width="80px">
          <el-form-item label="水费价格(元)" prop="waterPrice" label-width="110px">
            <el-input v-model="queryForm.waterPrice" @change="changeWaterPrice"></el-input>
          </el-form-item>
          <el-form-item label="电费价格(元)" prop="electricPrice" label-width="110px">
            <el-input v-model="queryForm.electricPrice" @change="changeElectricPrice"></el-input>
          </el-form-item>
        </el-form>
      </el-row>
      <el-row>
        <el-button size="small" @click="addDialog = true">添&nbsp;加</el-button>
      </el-row>
      <el-row :gutter="20">
        <div class="table">
          <el-table
              ref="table"
              :data="tableData"
              style="width: 100%"
              :default-sort = "{prop: 'date', order: 'descending'}">
            <el-table-column
                prop="floorName"
                label="楼层名称">
            </el-table-column>
            <el-table-column
                prop="roomName"
                label="房间名称">
            </el-table-column>
            <el-table-column
                prop="userName"
                label="用户姓名">
            </el-table-column>
            <el-table-column
                prop="water"
                label="水量">
            </el-table-column>
            <el-table-column
                prop="electric"
                label="电量">
            </el-table-column>
            <el-table-column
                prop="tenement"
                label="物业费">
            </el-table-column>
            <el-table-column
                prop="payment"
                label="应缴金额(元)"
                :formatter="formatterPayment">
            </el-table-column>
            <el-table-column
                prop="regular"
                label="租赁规则"
                :formatter="formatterReqular">
            </el-table-column>
            <el-table-column
                prop="contractDate"
                label="合同订立时间"
                :formatter="formatter">
            </el-table-column>
            <el-table-column
                prop="scheduledDate"
                label="预计到期时间"
                :formatter="formatter">
            </el-table-column>
            <el-table-column
                prop="createDate"
                label="创建时间"
                :formatter="formatter">
            </el-table-column>

          </el-table>
        </div>
        <div class="table">
          <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage"
              :page-sizes="[10, 20, 30, 40]"
              :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper"
              :total="total">
          </el-pagination>
        </div>
      </el-row>

    </el-dialog>
    <el-dialog
        :title="form.id?'编辑':'添加'"
        :visible.sync="addDialog"
        width="50%">

      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户" prop="userId" v-if="roomId">
          <!--<el-input v-model="form.userId"  maxlength="20" show-word-limit></el-input>-->
          <el-select v-model="form.userId" placeholder="请选择" style="width: 100%;">
            <el-option
                v-for="item in userOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="房间" prop="roomId" v-else>
          <el-select v-model="form.roomId" placeholder="请选择" style="width: 100%;">
            <el-option
                v-for="item in roomOptions"
                :key="item.id"
                :label="item.floorName + ' ' + item.name"
                :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
<!--        <el-form-item label="房间名称" prop="roomName">
          <el-input v-model="form.roomName"  maxlength="20" show-word-limit></el-input>
        </el-form-item>-->
        <el-form-item label="水量(吨)" prop="water">
          <el-input v-model="form.water"  maxlength="10" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="电量(度)" prop="electric">
          <el-input v-model="form.electric" maxlength="10" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="物业费" prop="tenement" >
          <el-input v-model="form.tenement" maxlength="10" show-word-limit></el-input>
        </el-form-item>
        <el-form-item label="合同订立时间" prop="contractDate">
          <el-date-picker type="date" placeholder="选择合同订立时间"
                          v-model="form.contractDate" style="width: 100%;"></el-date-picker>
        </el-form-item>
        <el-form-item label="租赁规则">
          <el-radio-group v-model="form.regular">
            <el-radio label="1">按月份</el-radio>
            <el-radio label="2">按季度</el-radio>
            <el-radio label="3">按年份</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="预计到期时间" prop="scheduledDate">
          <el-date-picker type="date" placeholder="选择预计到期时间" readonly
                          v-model="form.scheduledDate" style="width: 100%;"></el-date-picker>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm('form')">立即创建</el-button>
          <el-button @click="resetForm('form')">重置</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>


  </div>
</template>
<script>
  module.exports = {
    props: {
      visible:{
        type:Boolean,
        default:false,
      },
      roomId:{
        type:Number,
      },
      userId:{
        type:Number,
      }
    },
    data(){
      let checkNum = (rule, value, callback) => {
        if (!value) {
          return callback();
        }
        if (isNaN(value)){
          return callback(new Error('请输入数字值'));
        }
        return callback();
      };
      return {
        queryForm:{
          waterPrice: '',
          electricPrice: '',
        },
        tableData:[],
        cloneTableData:[],
        currentPage: 1,
        pageSize: 10,
        total: 0,
        show: false,
        url: '/leasing',
        addDialog: false,
        form:{
          roomId:'',
          userId:'',
          roomName:'',
          water:'',
          electric:'',
          tenement:'',
          contractDate:new Date(),
          regular:'1',
          scheduledDate:'',
        },
        rules: {
          userId: [
            { required: true, message: '请选择用户', trigger: 'blur' },
          ],
          roomId: [
            { required: true, message: '请选择房间', trigger: 'blur' },
          ],
          water: [
            { required: true, message: '请填写水量(吨)', trigger: 'change' },
            { validator: checkNum, message: '水量(吨)必须为数字值'}
          ],
          electric: [
            { required: true, message: '请填写电量(度)', trigger: 'change' },
            { validator: checkNum, message: '电量(度)必须为数字值'}
          ],
          tenement:[
            { validator: checkNum, trigger: 'blur' }
          ],
          contractDate:[
            { required: true, message: '请选择租赁时间', trigger: 'change' },
          ],
          regular:[
            { required: true, message: '请选择租赁规则', trigger: 'change' },
          ],

          // floorName: [
          //   { required: true, message: '请输入楼层名称', trigger: 'blur' },
          //   { max: 20, message: '长度最多 20 个字符', trigger: 'blur' }
          // ],
          // /*                    leasingDate: [
          //                         { type: 'date', required: true, message: '请选择租凭时间', trigger: 'change' }
          //                     ],*/
        },
        queryRules:{
          waterPrice: [
            { validator: checkNum, message: '价格必须为数字值'}
          ],
          electricPrice: [
            { validator: checkNum, message: '价格必须为数字值'}
          ],
        },
        userOptions:[],
        roomOptions:[],

      }
    },
    methods:{
      formatter(row, column) {
        let res = "";
        if (row[column.property] && row[column.property].length > 10){
          res = row[column.property].substring(0,10);
        }
        return res;
      },
      formatterReqular(row, column) {
        let res = "";
        switch (row.regular){
          case 1:
            res = "按月份";
            break;
          case 2:
            res = "按季度";
            break;
          case 3:
            res = "按年份";
            break;
        }
        return res;
      },
      formatterPayment(row, column){
        let res = "";
        if (!isNaN(row[column.property])){
          res = row[column.property];
        }
        return res;
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
      init(){
        let self = this;
        let page = {
          pageSize: self.pageSize,
          currentPage: self.currentPage,
          total: self.total,
          roomId: self.roomId,
          userId: self.userId,
        };
        axios({
          method: 'post',
          url: self.url + "/list",
          data: page,
          responseType: 'json'
        }).then(response => {
          // 请求成功
          let res = response.data;
          if (res && res.data){
            self.tableData = res.data;
            self.cloneTableData = JSON.parse(JSON.stringify(self.tableData));
            // 设置默认用户
            if (self.tableData[0]){
              self.form.userId = self.tableData[0].userId || "";
              self.form.roomId = self.tableData[0].roomId || "";
            }
            // 计算价格
            self.totalPrice("");
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
      selectDate(value){
        if (this.form.contractDate){
          let num = 1;
          if (value === '1'){
            num = 1;
          }else if(value === '2'){
            num = 3;
          }else if (value === '3'){
            num = 12;
          }
          let temDate = new Date(this.form.contractDate.getFullYear(),this.form.contractDate.getMonth(),this.form.contractDate.getDate());
          this.form.scheduledDate = new Date(temDate.setMonth(temDate.getMonth() + num))
        }
      },
      initUser(){
        let self = this;
        axios({
          method: 'post',
          url: "/user/findAllUser",
          responseType: 'json'
        }).then(response => {
          // 请求成功
          let res = response.data;
          console.log(res);
          if (res){
            self.userOptions = res;
          }
        }).catch(error => {
          // 请求失败，
          console.log(error);
          this.$message({
            message: error,
            type: 'error',
          });
        });
      },
      initRoom(){
        let self = this;
        axios({
          method: 'post',
          url: "/room/findAllRoom",
          responseType: 'json'
        }).then(response => {
          // 请求成功
          let res = response.data;
          console.log(res);
          if (res){
            self.roomOptions = res;
          }
        }).catch(error => {
          // 请求失败，
          console.log(error);
          this.$message({
            message: error,
            type: 'error',
          });
        });
      },
      resetForm(formName){
        this.$refs[formName].resetFields();
      },
      submitForm(formName) {
        let self = this;
        if (self.roomId){
          self.form.roomId = self.roomId;
        }
        if (self.userId){
          self.form.userId = self.userId;
        }
        this.$refs[formName].validate((valid) => {
          if (valid) {
            self.add(self.form);
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      add(value){
        let self = this;
        console.log('value',value);
        axios({
          method: 'post',
          url: self.url + "/add",
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

                self.addDialog = false;
                self.init();
                self.resetForm('form');
              }
            });
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
      changeWaterPrice(val){
        console.log(val);
        let water = parseFloat(val);
        if (!isNaN(water)){
          this.totalPrice(water);
          localStorage.setItem("queryForm.waterPrice",val);
        }
      },
      changeElectricPrice(val){
        console.log(val);
        let electric = parseFloat(val);
        if (!isNaN(electric)){
          this.totalPrice("");
          localStorage.setItem("queryForm.electricPrice",val);
        }
      },
      totalPrice(val){
        let self = this;
        let waterPrice = parseFloat(self.queryForm.waterPrice);
        if (isNaN(waterPrice)){
          waterPrice = 0;
        }
        if (self.cloneTableData){
          self.tableData = JSON.parse(JSON.stringify(self.cloneTableData))
          let electricPrice = parseFloat(self.queryForm.electricPrice)
          if (isNaN(electricPrice)){
            electricPrice = 0;
          }
          // 计算水电价格
          for(let i = 0, e = self.tableData.length; i < e; ++i){
            let current = self.tableData[i];
            current = self.calc(waterPrice,electricPrice,current);
          }
          // 计算应缴
          for(let i = 0, e = self.tableData.length; i < e; ++i){
            let current = self.tableData[i];
            let previous;
            if (i + 1 < e){
              previous = self.tableData[i +1];
            }
            if (previous){
              current.payment = (current.payment - previous.payment).toFixed(1);
            }else{
              current.payment = current.payment.toFixed(1);
            }
          }

        }
      },
      calc(waterPrice,electricPrice,current,previous){
        if (current){
          let payment = parseFloat(current.payment);
          if (isNaN(payment)){
            payment = 0;
          }
          let water = parseFloat(current.water);
          if (isNaN(water)){
            water = 0;
          }
          let electric = parseFloat(current.electric);
          if (isNaN(electric)){
            electric = 0;
          }
          current.payment += electric*electricPrice;
          current.payment += water*waterPrice;
          if (previous){
            let previousPayment = parseFloat(previous.payment);
            if (isNaN(previousPayment)){
              previousPayment = 0;
            }
            let previousWater = parseFloat(previous.water);
            if (isNaN(previousWater)){
              previousWater = 0;
            }
            let previousElectric = parseFloat(previous.electric);
            if (isNaN(previousElectric)){
              previousElectric = 0;
            }
            previous.payment += previousElectric*electricPrice;
            previous.payment += previousWater*waterPrice;
            current.payment = current.payment - previous.payment;
          }
          return current;
        }
      },


    },
    watch:{
      visible:function(value){
        if (value){
          this.init();
        }
        console.log('this.visible',this.visible);
        console.log('this.roomId',this.roomId);
        console.log('this.userId',this.userId);
        this.show = value;
        this.$emit('update:visible',this.visible)
      },
      show:function (value){
        console.log('show .value',value);
        this.$emit('update:visible',value)
      },
      'form.regular':function (value){
        if (value){
          this.selectDate(value);
        }
      },
      'form.contractDate':function(value){
        if (value){
          this.selectDate(this.form.regular);
        }
      },
      // 'queryForm.waterPrice':function(value){
      //   if (value){
      //     console.log('waterPrice',value);
      //   }
      //   // localStorage.setItem('');
      // },
      // 'queryForm.electricPrice':function(value){
      //
      // },


  //     immediate: true,
  // 　　 deep: true,
  //     visible:{
  //       handle(value){
  //         // console.log('this.visible',this.visible);
  //         this.show = value;
  //         // this.$emit('update:visible',value)
  //       },
  //       // immediate: true,
  //       // deep: true,
  //     },
  //     show:{
  //       handle(value){
  //         this.$emit('update:visible',value)
  //       },
  //       // immediate: true,
  //       // deep: true,
  //     }

    },

    // computed:{
    //   show:{
    //     get(val){
    //       console.log('get show',this.visible);
    //       console.log('get val',val);
    //       return this.visible;
    //     },
    //     set(val){
    //       // console.log('set show',this.visible);
    //       // console.log('set val',val);
    //       // return val;
    //     }
    //
    //   }
    // },
    mounted(){
      this.selectDate(1);
    },
    created: function () {
      // this.show = this.visible;
      // console.log('this.visible',this.visible);
      this.initUser();
      this.initRoom();
      let waterPrice = localStorage.getItem("queryForm.waterPrice");
      if (!isNaN(parseFloat(waterPrice))){
        this.queryForm.waterPrice = waterPrice;
      }
      let electricPrice = localStorage.getItem("queryForm.electricPrice");
      if (!isNaN(parseFloat(electricPrice))){
        this.queryForm.electricPrice = electricPrice;
      }
    }
  }
</script>
<style>

</style>