define(function () {
  return {
    getTextWidth: function (s, fontInfo) {
      if (!s)
        return 0;
      s = s.split("&nbsp;").join("W").split("\&nbsp;").join("W");
      if (!fontInfo)
        fontInfo = $(document.body).css("fontSize") + " " + $(document.body).css("fontFamily");
      var canvas = document.createElement("canvas");
      var ctx = canvas.getContext("2d");
      ctx.font = fontInfo; //"20pt Arial";  // This can be set programmaticly from the element's font-style if desired
      var textWidth = ctx.measureText(s).width;
      return textWidth + 1;
    },

    addChild: function (wParent, wChild) {
      //setTimeout(function () {
      var p = null;
      if (wChild.component) {
        try {
          p = wChild.component.getParent();
        } catch (t) {}
      } else {
        console.log("Wrong child", wChild);
      }

      if ((p != null) && (p == wParent.component))
        return; // the parent is not going to be changed

      try {
        if (p)
          p.removeChild(wChild.component);
      } catch (t) {
        console.log(t);
        return;
      }

      try {
        wParent.component.addChild(wChild.component);
      } catch (t) {
        console.log(t);
        return;
      }

      var parentType = wParent.type;
      if ((parentType == "form") || (parentType == "verticalbox") || (parentType == "tab") || (parentType == "groupbox")) {
        wChild.component.style.split("display:inline-block").join("display:block");
        setTimeout(function () {
          require(["dojo", "dojo/domReady!"], function (dojo) {
            wChild.component.domNode.style.display = "block";
            dojo.style(wChild.component, "display", "block");
          });
        }, 0);

      }

      require(["dojo", "dojo/domReady!"], function (dojo) {
        $(wChild.component.domNode).css("vertical-align", "top");
      });
      //}, 50);
    },

    getInitialBounds: function (node) {
      var retVal = {};
      retVal.leftMargin = node.leftMargin;
      //retVal.rightMargin = node.rightMargin;
      retVal.topMargin = node.topMargin;
      //retVal.bottomMargin = node.bottomMargin;

      retVal.leftPadding = node.leftPadding;
      retVal.rightPadding = node.rightPadding;
      retVal.topPadding = node.topPadding;
      retVal.bottomPadding = node.bottomPadding;

      retVal.leftBorder = node.leftBorder;
      retVal.rightBorder = node.rightBorder;
      retVal.topBorder = node.topBorder;
      retVal.bottomBorder = node.bottomBorder;

      retVal.horizontalSpacing = node.horizontalSpacing;
      retVal.verticalSpacing = node.verticalSpacing;

      if ((!node.horizontalAlignment) || (node.horizontalAlignment < 0) || (node.horizontalAlignment == "-1"))
        retVal.horizontalAlignment = "LEFT";
      else
      if ((node.horizontalAlignment > 0) || (node.horizontalAlignment == "+1"))
        retVal.horizontalAlignment = "RIGHT";

      if ((!node.verticalAlignment) || (node.verticalAlignment < 0) || (node.verticalAlignment ==
          "-1"))
        retVal.verticalAlignment = "TOP";
      else
      if ((node.verticalAlignment > 0) || (node.verticalAlignment == "+1"))
        retVal.verticalAlignment = "BOTTOM";

      if (node.minimumRelativeWidth)
        retVal.minimumRelativeWidth = node.minimumRelativeWidth;
      if (node.minimumRelativeHeight)
        retVal.minimumRelativeHeight = node.minimumRelativeHeight;

      if (node.preferredRelativeWidth)
        retVal.preferredRelativeWidth = node.preferredRelativeWidth;
      if (node.preferredRelativeHeight)
        retVal.preferredRelativeHeight = node.preferredRelativeHeight;

      if (node.maximumRelativeWidth)
        retVal.maximumRelativeWidth = node.maximumRelativeWidth;
      if (node.maximumRelativeHeight)
        retVal.maximumRelativeHeight = node.maximumRelativeHeight;

      return retVal;
    },

    adjustBounds: function (bounds) {
      if (bounds.skipAdjustingPadding)
        return bounds;

      // adjusting padding to start padded children at relative coordiates (0;0)...
      if (bounds.leftPadding || bounds.rightPadding) {
        if (!bounds.leftPadding)
          bounds.leftPadding = 0;
        if (!bounds.rightPadding)
          bounds.rightPadding = 0;
        bounds.rightPadding = bounds.leftPadding + bounds.rightPadding;
        bounds.leftPadding = 0;
      }
      if (bounds.topPadding || bounds.bottomPadding) {
        if (!bounds.topPadding)
          bounds.topPadding = 0;
        if (!bounds.bottomPadding)
          bounds.bottomPadding = 0;
        bounds.bottomPadding = bounds.topPadding + bounds.bottomPadding;
        bounds.topPadding = 0;
      }
      return bounds;
    }

  };
});