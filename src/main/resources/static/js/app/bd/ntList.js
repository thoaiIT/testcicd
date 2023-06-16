var ntList = null;
$(document).ready(function () {
  //검색
  const initSearchForm = {
    category: "", //공지구분
    status: [], //공지상태
    type: [], //공지유형
    periodType: "openDate", //기간검색 대상
    fromDate: "", //기간검색 시작일
    toDate: "", //기간검색 종료일
    criteria: "subject", //검색 대상
    keyword: "", //검색어
  };

  ntList = new Vue({
    el: "#ntList",
    components: {
      vuejsDatepicker,
    },
    data: {
      //Dummy
      category: [
        // TODO 코드명 미정으로 추후 수정
        { code: "SY", text: "System" },
        { code: "MK", text: "Marketing" },
        { code: "CM", text: "Common" },
      ],
      tStat: [
        { id: "O", text: "Open" },
        { id: "R", text: "Ready" },
        { id: "C", text: "Close" },
      ],
      tType: [
        { id: "I", text: "Important" },
        { id: "N", text: "Normal" },
      ],
      periodType: [
        { id: "openDate", text: "Open Date" },
        { id: "regDate", text: "Registration Date" },
        { id: "updDate", text: "Updated Date" },
      ],
      from: null,
      to: null,
      criteria: [
        { id: "subject", text: "Subject" },
        { id: "regName", text: "Writer" },
        { id: "updName", text: "Updater" },
      ],
      //검색 : 상태값 전체선택
      search: Object.assign({}, initSearchForm),
      termsPicked: "",
      is_searched: false,
      list: [],
    },
    mixins: [pagingMixin], //common.js
    methods: {
      setSearchPeriod(e) {
        this.$refs.defaultPick.click();
      },
      init() {
        // 검색 기간 오늘로 설정
        this.setSearchPeriod();
        this.pagination.cntPerPage = 2;

        // 공지 정보 페이지에서 왔는지 확인하여 기존 검색 설정값 복원
        if (
          (sessionStorage.getItem("from") == "ntWrite" ||
            sessionStorage.getItem("from") == "ntView") &&
          sessionStorage.getItem("searchNtList")
        ) {
          this.search = JSON.parse(sessionStorage.getItem("searchNtList"));
          this.pagination.currentPage = this.search.page;
          this.is_searched = true;
          sessionStorage.clear();
        }

        // 페이지 리스트 작성
        this.getList();

        // 페이지  UI요소 초기 설정
        this.uiInit();
      },
      uiInit() {
        this.from = $("#from")
          .datepicker({ dateFormat: "yy-mm-dd" })
          .on("change", function () {
            ntList.to.datepicker("option", "minDate", fnGetDate(this));
          });

        this.to = $("#to")
          .datepicker({ dateFormat: "yy-mm-dd" })
          .on("change", function () {
            ntList.from.datepicker("option", "maxDate", fnGetDate(this));
          });

        // script.js에 정의되어 있는 공통 UI스크립트 실행
        uiInit();
      },
      convertVal: function (val) {
        return val == "Y" ? "O" : "-";
      },
      resetForm() {
        this.search = Object.assign({}, initSearchForm);
        this.pagination.currentPage = 1;
        this.is_searched = false;
        $(".checkAll").prop("checked", false);
        this.setSearchPeriod();

        this.getList();
      },
      getList() {
        let params = Object.assign({}, this.search);
        params.currentPage = this.pagination.currentPage;
        params.cntPerPage = this.pagination.cntPerPage;

        fnItemFiltering(params);

        console.log("params:", params);
        //apiConfig.js
        this.$Ajax(apiRest.apiNtList, params, this.listCallBack);
      },
      listCallBack(data) {
        if (data.success) {
          let startNo =
            data.TOTAL_COUNT -
            (ntList.pagination.currentPage - 1) * ntList.pagination.cntPerPage;
          $.each(data.LIST, function () {
            let me = this;
            me.articleNo = startNo--;
            me.category = ntList.category.filter(
              (item) => item.code === me.bd10Category
            )[0].text;
            me.status = ntList.tStat.filter(
              (item) => item.id === me.bd10Status
            )[0].text;
            me.type = ntList.tType.filter(
              (item) => item.id === me.bd10Type
            )[0].text;
            me.regDate = moment(me.bd10RegDate).format("YYYY-MM-DD hh:mm:ss");
            me.updDate = moment(me.bd10UpdDate).format("YYYY-MM-DD hh:mm:ss");
          });

          this.list = data.LIST;
          this.setPagination(data.TOTAL_COUNT);
        } else {
          this.$commonAlert(data.message);
        }
      },
      goSearch() {
        this.pagination.currentPage = 1;
        this.is_searched = true;
        this.getList();
      },
      setTermTg: function (e) {
        let type = $(e.target).data("type");
        let calDay = this.$toDay();
        this.search.toDate = calDay;
        if (this.termsPicked !== 0) {
          let schStartDt = moment(moment(), "MM-DD-YYYY").add(
            this.termsPicked * -1,
            type
          );
          calDay = this.$formatDate(schStartDt, "YYYY-MM-DD");
        }
        this.search.fromDate = calDay;
      },
      goDetail(sdate, id) {
        if (this.is_searched) {
          this.search.page = this.pagination.currentPage;
          sessionStorage.setItem("searchNtList", JSON.stringify(this.search));
        }

        if (moment().isBefore(sdate)) {
          location.href = "/bd/write?guid=" + id;
        } else {
          location.href = "/bd/view?guid=" + id;
        }
      },
    },
    created() {},
    mounted: function (e) {
      this.init();
    },
    watch: {
      termsPicked() {
        //console.log('watch : ', this.termsPicked)
      },
    },
    computed: {
      getEndDate() {
        return this.$toDay();
      },
    },
  });
});
