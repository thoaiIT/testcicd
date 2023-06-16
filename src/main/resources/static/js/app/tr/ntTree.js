var ntTree = null;
$(document).ready(function () {
  ntTree = new Vue({
    el: "#ntTree",
    data: {
      group1Data: [
        {
          dataId: "field1",
          text: "Field 1",
          active: true,
          subChild: [
            {
              dataId: "field11",
              text: "Field 11",
              active: true,
              subChild: [
                { dataId: "field111", text: "Field 111", active: true },
              ],
            },
            {
              dataId: "field12",
              text: "Field 12",
              active: true,
              subChild: [
                { dataId: "field121", text: "Field 121", active: true },
                { dataId: "field122", text: "Field 122", active: true },
              ],
            },
          ],
        },
        {
          dataId: "field2",
          text: "Field 2",
          active: true,
          subChild: [
            {
              dataId: "field22",
              text: "Field 22",
              active: true,
              subChild: [
                { dataId: "field222", text: "Field 222", active: true },
              ],
            },
          ],
        },
      ],
      group2Data: [],
    },
    methods: {
      allowDrop(event) {
        event.preventDefault();
      },
      drag(event) {
        event.dataTransfer.setData(
          "text",
          `${event.target.dataset.id}_${event.target.dataset.path}`
        );
      },
      drop(event) {
        var [id, path] = event.dataTransfer.getData("text").split("_");
        // const pathArray = path.split("-");

        // const tempData = [...group1Data]
        // let tempSubchild;
        // let filter;

        // for (key in pathArray){
        //   filter = tempData.filter(i=>i.dataId === key)
        //   tempSubchild = filter.subChild
        // }

        // console.log(parentHierarchy);
        var field = document.querySelector('[data-path="' + path + '"]');
        event.target.appendChild(field);
      },
      renderNode(node, depth = 0) {
        if (!node.subChild)
          return `
            <div
                draggable="true"
                ondragstart="drag(event)"
                data-id="${node.dataId}"
                data-path="${node.path}"
                class="field"
                style="margin-left: ${depth * 10}px"
                >
                 <input type="checkbox" id="${node.dataId}"/>
                <label for="${node.dataId}">${node.text}</label>

            </div>
            `;
        else
          return `
            <div
                draggable="true"
                ondragstart="drag(event)"
                data-id="${node.dataId}"
                data-path="${node.path}"
                class="field"
                style="margin-left: ${depth * 10}px"
                >
                <input type="checkbox" id="${node.dataId}"/>
               <label for="${node.dataId}">${node.text}</label>
               <div>    ${node.subChild
                 .map((item) => this.renderNode(item, depth + 1))
                 .join("")}
               </div>
            </div>
    `;
      },
      checkParent(element) {
        if (element.prop("checked")) {
          var parent = element
            .parent()
            .parent()
            .find('> input[type="checkbox"]');

          if (parent.length) {
            parent.prop("checked", true);
            checkParent(parent);
          }
        }
      },
      handleInitData(data, path = "") {
        return data.map((item) => {
          const pathTemp = path ? path + "-" + item.dataId : item.dataId;
          return {
            ...item,
            path: pathTemp,
            ...(item.subChild
              ? { subChild: this.handleInitData(item.subChild, pathTemp) }
              : {}),
          };
        });
      },
      init() {
        const handledGroup1Data = this.handleInitData(this.group1Data);
        console.log(handledGroup1Data);
        $("#group-1")[0].innerHTML = handledGroup1Data
          .map((item) => this.renderNode(item))
          .join("");
      },
    },
    created() {},
    mounted: function (e) {
      this.init();
    },
  });
});
