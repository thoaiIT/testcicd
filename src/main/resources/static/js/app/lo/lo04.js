'use strict'
let vm;
$(function() {
vm = new Vue({
    el: '#wrap',
    data: {
        mobile: '',
        code: '',
        step: 0,
        step1Message: '',
        step2Message: '',
        timer: undefined,
        timeCount: 180
    },
    created() {},
    mounted() {},
    computed: {
        isInvalidMobile() {
            return !/^((01[1|6|7|8|9])[1-9]+[0-9]{6,7})|(010[1-9][0-9]{7})$/.test(this.mobile);
        },
        timeCountDisp() {
            const me = this;
            const min = Math.floor(me.timeCount / 60);
            const sec = me.timeCount - (min * 60);
            return me.fnTimePad(min) + ':' + me.fnTimePad(sec);
        }
    },
    methods: {
        fnMobileProcess() {
            //console.log('@@ methods - fnMobileProcess()', arguments, this);
            const me = this;
            me.$commonAxios({ url: '/lo/04/01', type: 'GET' }, {
                mobile: me.mobile
            }, function(data) {
                //console.log('success', arguments, this);
                me.step1Message = data;
                if (data === 'success') {
                    me.step = 1;
                    me.fnTimerStart();
                }
            });
        },
        fnMobileCheck() {
            //console.log('@@ methods - fnMobileCheck()', arguments, this);
            const me = this;
            me.$commonAxios({ url: '/lo/04/02', type: 'POST' }, {
                mobile: me.mobile,
                code: me.code
            }, function(data) {
                //console.log('success', arguments, this);
                me.step2Message = data;
                if (data === 'success') {
                    me.step = 2;
                    me.$commonAlert('문자인증이 완료되었습니다.\n[확인] 클릭 시 비밀번호 재설정이 진행됩니다.', function() {
                        location.href = '/lo/02';
                    });
                } else {
                    me.$refs.code.classList.remove('success');
                    me.$refs.code.classList.add('error');
                }
            });
        },
        fnTimerStart() {
            const me = this;
            me.timeCount = 180;
            me.timer = setInterval(function() {
                if (me.timeCount > 0) me.timeCount --;
                else me.timeCount = 0;
            }, 1000);
        },
        fnTimePad(time) {
            return (time < 10 ? '0' : '') + time;
        }
    }
});
});