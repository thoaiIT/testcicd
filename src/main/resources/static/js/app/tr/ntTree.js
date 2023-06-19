var ntTree = null;
$(document).ready(function () {
  ntTree = new Vue({
    el: "#ntTree",
    data: {
      treeViewLeft: [
        {
          id: 1,
          label: "Item 1",
          checked: false,
          parent: "",
          children: [
            {
              id: 1.1,
              label: "Item 1.1",
              checked: false,
              parent: 1,
            },
            {
              id: 1.2,
              label: "Item 1.2",
              checked: false,
              parent: 1,
            },
          ],
        },
        {
          id: 2,
          label: "Item 2",
          checked: false,
          children: [
            {
              id: 2.1,
              label: "Item 2.1",
              checked: false,
              parent: 2,
            },
            {
              id: 2.2,
              label: "Item 2.2",
              checked: false,
              parent: 2,
            },
            {
              id: 2.3,
              label: "Item 2.3",
              checked: false,
              parent: 2,
            },
          ],
        },
      ],
      treeViewRight: [],
    },
    methods: {
      // allowDrop(event) {
      //   event.preventDefault();
      // },
      // drag(event) {
      //   event.dataTransfer.setData(
      //     "text",
      //     `${event.target.dataset.id}_${event.target.dataset.path}`
      //   );
      // },
      // drop(event) {
      //   console.log(event);

      //   $("#group-2")[0].innerHTML = this.group2Data
      //     .map((item) => this.renderNode(item))
      //     .join("");
      //   $("#group-1")[0].innerHTML = this.handledGroup1Data
      //     .map((item) => this.renderNode(item))
      //     .join("");
      // },
      // renderNode(node, depth = 0) {
      //   if (!node.subChild)
      //     return `
      //     <div
      //         draggable="true"
      //         ondragstart="drag(event)"
      //         data-id="${node.dataId}"
      //         data-path="${node.path}"
      //         class="field ${node.active ? "active" : ""}"
      //         style="margin-left: ${depth * 32}px"
      //         >
      //           <input type="checkbox"></input>${node.text}
      //     </div>
      //     `;
      //   else
      //     return `
      //     <div
      //         draggable="true"
      //         ondragstart="drag(event)"
      //         data-id="${node.dataId}"
      //         data-path="${node.path}"
      //         class="field ${node.active ? "active" : ""}"
      //         style="margin-left: ${depth * 10}px;"
      //         >
      //         <input type="checkbox"></input>${node.text}
      //     </div>
      //     ${node.subChild
      //       .map((item) => this.renderNode(item, depth + 1))
      //       .join("")}
      //     `;
      // },
      // checkParent(element) {
      //   if (element.prop("checked")) {
      //     var parent = element
      //       .parent()
      //       .parent()
      //       .find('> input[type="checkbox"]');

      //     if (parent.length) {
      //       parent.prop("checked", true);
      //       checkParent(parent);
      //     }
      //   }
      // },
      // handleInitData(data, path = "", parent = "") {
      //   return data.map((item) => {
      //     const pathTemp = path ? path + "-" + item.dataId : item.dataId;
      //     return {
      //       ...item,
      //       parent: parent,
      //       path: pathTemp,
      //       ...(item.subChild
      //         ? {
      //             subChild: this.handleInitData(
      //               item.subChild,
      //               pathTemp,
      //               item.dataId
      //             ),
      //           }
      //         : {}),
      //     };
      //   });
      // },
      // init() {
      //   const handledG1Data = this.handleInitData(this.group1Data);
      //   console.log(handledG1Data);
      //   $("#group-1")[0].innerHTML = handledG1Data
      //     .map((item) => this.renderNode(item))
      //     .join("");
      // },
      updateParentCheckboxes(item) {
        const child = item.children;
        console.log(child);
        if (item.checked) {
          console.log("true");
          child.forEach((data) => (data.checked = true));
        } else {
          child.forEach((data) => (data.checked = false));
        }
      },
      updateParentCheckboxesRecursive(items) {
        items.forEach((item) => {
          if (item.children && item.children.length) {
            const allChecked = item.children.every((child) => child.checked);
            item.checked = allChecked;
            this.updateParentCheckboxesRecursive(item.children);
          }
        });
      },
      updateChildCheckboxes(item) {
        const parent = this.treeViewLeft.filter(
          (data) => data.id === item.parent
        );
        if (parent[0].children.every((data) => data.checked)) {
          parent[0].checked = true;
        } else {
          parent[0].checked = false;
        }
      },
      addItems() {
        console.log("add item");
        const selectedItems = this.treeViewLeft.filter((item) => item.checked);
        selectedItems.forEach((item) => {
          const newItem = {
            id: item.id,
            label: item.label,
            checked: false,
          };
          this.treeViewRight.push(newItem);
          this.updateParentCheckboxesRecursive(this.treeViewLeft);
        });
      },
      deleteItems() {
        const selectedItems = this.treeViewRight.filter((item) => item.checked);
        selectedItems.forEach((item) => {
          const index = this.treeViewRight.indexOf(item);
          this.treeViewRight.splice(index, 1);
          this.updateParentCheckboxesRecursive(this.treeViewLeft);
        });
      },
    },
  });
});
