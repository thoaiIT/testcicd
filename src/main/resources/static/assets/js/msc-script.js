//MIT 라이선스(MIT)
//Copyright (c) 2015 Bitwiser.in

(function (root, factory) {
    if (typeof define === 'function' && define.amd) {
        define([], factory);
    } else if (typeof module === 'object' && module.exports) {
        module.exports = factory();
    } else {
        var expData = factory();
        for(var key in expData) {
            if (expData.hasOwnProperty(key)) {
                root[key] = expData[key];
            }
        }
  }
}(this, function(){
    function ce(tag, clas, txt) {
        var ele = document.createElement(tag);
        ele.setAttribute('class', clas);
        if(typeof txt === 'undefined' || txt === null){
          return ele;
        }
        
        //var tn = document.createTextNode(txt);
        //ele.appendChild(tn);
        
        ele.innerHTML = txt;
        return ele;
    }
    var KEY_ESC = 27;
    var KEY_ENTER = 13;

    function buildUI(title, sub, onOk, onCancel, type) {
        if (typeof window === 'undefined') {
            throw 'Cannot use this in node.';
        }
        var prev = document.getElementsByClassName('msc-confirm');
        if(prev.length > 0){
            document.body.removeChild(prev[0]);
        }

        var options = {
            title: '',
            subtitle: '',
            onOk: null,
            onCancel: null,
            okText: 'Confirm',
            cancelText: 'Cancel',
            placeholder: 'Enter value',
            dismissOverlay: false,
            closeOnEscape: true,
            defaultValue: ''
        };

        if(typeof title === 'object') {
            for(var key in title) {
                options[key] = title[key];
            }
            if(typeof options.onOk !== 'function') {
                options.onOk = null;
            }
            if(typeof options.onCancel !== 'function') {
                options.onCancel = null;
            }
        } else {
            options.title = (typeof title === 'string') ? title : options.title;
            options.subtitle = (typeof sub === 'string') ? sub : options.subtitle;
            options.onOk = (typeof onOk === 'function') ? onOk : options.onOk;
            options.onCancel = (typeof onCancel === 'function') ? onCancel : options.onCancel;

            if(typeof sub === 'function') {
                options.onOk = sub;
            }
        }

        var modal = ce('div', 'modal show'),
            dialog = ce('div', 'modal-dialog modal-sm modal-dialog-centered'),
            overlay = ce('div', 'modal-backdrop fade show'),
            closeBtn = ce('button', 'btn-close');
        
        if(options.dismissOverlay) {
            overlay.addEventListener("click", destroy);
        }

        closeBtn.addEventListener('click', destroy);

        var content = ce('div', 'modal-content'),
            TitleBox = ce('div', 'modal-header'),
            cTitle = ce('p', 'modal-title', options.title),
            body = ce('div', 'modal-body text-center pb-1'),
            contentText = ce('div','p-1'),
            actionbox = ce('div', 'modal-footer pt-3 row row-sm justify-content-center'),
            action = ce('div', 'col-md-6'),
            action2 = ce('div', 'col-md-6'),
            okBtn = ce('button', 'btn btn-primary w-100', options.okText),
            cancelbtn = ce('button', 'btn btn-secondary w-100', options.cancelText),
            input = ce('input', 'msc-input');

        TitleBox.appendChild(closeBtn);
        TitleBox.appendChild(cTitle);
        contentText.appendChild(ce('p','', options.subtitle));
        body.appendChild(contentText);

        if(type !== "alert") {
            action2.appendChild(cancelbtn);
            actionbox.appendChild(action2);
            cancelbtn.addEventListener('click', cancel);
        }

        action.appendChild(okBtn);
        actionbox.appendChild(action);

        okBtn.addEventListener('click', ok);

        content.appendChild(TitleBox);
        content.appendChild(body);
        content.appendChild(actionbox);

        dialog.appendChild(content);

        modal.appendChild(dialog);

        document.body.appendChild(modal);
        document.body.appendChild(overlay);

        document.body.classList.add('modal-open');
        document.body.style.overflow = 'hidden';
        document.body.style.padding = '0px';

        modal.style.display = 'block';
        content.classList.add('msc-confirm--animate')

        if(type === "prompt") {
            input.setAttribute("type", "text");
            input.setAttribute("placeholder", options.placeholder);
            input.value = options.defaultValue;
            input.addEventListener("keyup", function(e) {
                if(e.keyCode === KEY_ENTER) {
                    ok();
                }
            });
            body.appendChild(input);
            input.focus();
        }else if(type==="alert") {
            okBtn.focus();
        }else {
            cancelbtn.focus();
        }

        document.addEventListener('keyup', _hide);

        function destroy() {
            closeBtn.removeEventListener('click', destroy);
            okBtn.removeEventListener('click', ok);
            cancelbtn.removeEventListener('click', cancel);
            if(options.dismissOverlay) {
                overlay.removeEventListener("click", destroy);
            }
            document.removeEventListener('keyup', _hide);
            document.body.removeChild(modal);
            document.body.removeChild(overlay);

            document.body.classList.remove('modal-open');
            document.body.style= '';
        }

        function ok() {
            destroy();
            if(options.onOk !== null) {
                if(type === "prompt") {
                    options.onOk(input.value);
                }else {
                    options.onOk();
                }
            }
        }

        function cancel() {
            destroy();
            if(options.onCancel !== null) {
                options.onCancel();
            }
        }

        function _hide(e) {
            if(options.closeOnEscape && e.keyCode == KEY_ESC) {
                destroy();
            }
        }
    };
    var exportData = {
        mscConfirm: function(title, sub, onOk, onCancel) {
            buildUI(title, sub, onOk, onCancel, "confirm");
        },
        mscPrompt: function(title, sub, onOk, onCancel) {
            buildUI(title, sub, onOk, onCancel, "prompt");
        },
        mscAlert: function(title, sub, onOk, onCancel) {
            buildUI(title, sub, onOk, onCancel, "alert");
        },
        mscClose: function() {
            var prev = document.getElementsByClassName('msc-confirm');
            if(prev.length > 0){
                document.body.removeChild(prev[0]);
            }
        }
    };
    return exportData;
}));
