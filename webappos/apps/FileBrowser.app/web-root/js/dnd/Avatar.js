
define([
	'dojo/_base/window',
	'dojo/_base/array',
	'dojo/_base/declare',
	'dojo/dom-construct',
	'dojo/dom-class',
	'dojo/dom-attr',
	'dojo/dnd/Avatar'],
function(window, array, declare, domConstruct, domClass, domAttr, Avatar) {
        

	// preloading icons. Note: Can't be done in constructor, because it is only called when used
	var images = {
		folder: new Image(),
		files: new Image(),
		file: new Image()
	};

	var canvas = document.createElement('canvas');
	canvas.width = 64;
	canvas.height = 64;
	var ctx = canvas.getContext('2d');

	images.folder.onload=function() {
           ctx.drawImage(images.folder, 0, 0, 64, 64);
           var img2 = new Image();
           img2.src = canvas.toDataURL("image/jpeg");
           img2.onload = function() {
             images.folder = img2;
           };
	};
	images.folder.src = require.toUrl('rfe') + '/../mimes/folder.png'; // by SK
	images.files.onload=function() {
           ctx.drawImage(images.files, 0, 0, 64, 64);
           var img2 = new Image();
           img2.src = canvas.toDataURL("image/jpeg");
           img2.onload = function() {
             images.files = img2;
           };
	};
	images.files.src = require.toUrl('rfe') + '/../mimes/files.png'; // by SK
	images.file.onload=function() {
           ctx.drawImage(images.file, 0, 0, 64, 64);
           var img2 = new Image();
           img2.src = canvas.toDataURL("image/jpeg");
           img2.onload = function() {
             images.file = img2;
           };
	};
	images.file.src = require.toUrl('rfe') + '/../mimes/file.png'; // by SK

	return declare([Avatar], {

		constructor: function() {
			this.images = images;

			this.isA11y = domClass.contains(window.body(), "dijit_a11y");
			var a = domConstruct.create("table", {
				"class": "dojoDndAvatar",
				style: {
					position: "absolute",
					zIndex: "1999",
					margin: "0px"
				}
			}),
			source = this.manager.source, node,
			b = domConstruct.create("tbody", null, a),
			tr = domConstruct.create("tr", null, b),
			td = domConstruct.create("td", null, tr),
			span = domConstruct.create("span", {
				innerHTML: source.generateText ? this._generateText(): ""
			}, td);

			// we have to set the opacity on IE only after the node is live
			domAttr.set(tr, {
				"class": "dojoDndAvatarHeader",
				style: {opacity: 0.9}
			});

			node = domConstruct.create('div');
			node.appendChild(this.createIcon());
			node.id = "";
			tr = domConstruct.create("tr", null, b);
			td = domConstruct.create("td", null, tr);
			td.appendChild(node);
			domAttr.set(tr, {
				"class": "dojoDndAvatarItem",
				style: {opacity: 0.9}
			});

			this.node = a;
		},

		createIcon: function() {
			var img,
			nodes = this.manager.nodes,
			source = this.manager.source,
			isDir = array.some(nodes, function(node) {
				var obj = source.getObject(node);
				return obj.dir;
			}, this);

			if (isDir) {
				img = this.images.folder;
			}
			else if (nodes.length > 1) {
				img = this.images.files;
			}
			else {
				img = this.images.file;
			}
			return img;
		},

		_generateText: function() {
			// summary: generates a proper text to reflect copying or moving of items
			var numItems = this.manager.nodes.length.toString(),
			action = this.manager.copy ? 'Copy to': 'Move to';

			return numItems + ', ' + action;
		}
	});

});
