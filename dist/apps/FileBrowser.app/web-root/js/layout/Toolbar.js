define([
	'dojo/_base/lang',
	'dojo/_base/declare',
	'dojo/_base/array',
	'dojo/on',
	'dojo/aspect',
	'dojo/dom-construct',
	'dojo/query',
	'dijit/registry',
	'dijit/Toolbar',
	'dijit/ToolbarSeparator',
	'dijit/form/Button',
	'dijit/form/Select',
	'rfe/config/fileObject',
	'rfe/SearchBox'
], function(lang, declare, array, on, aspect, domConstruct, query, registry, Toolbar, ToolbarSeparator, Button, Select, fileObject, SearchBox) {

	/**
	 * @class rfe.layout.Toolbar
	 * @extends dijit.Toolbar
	 * @property {rfe} rfe reference to remoteFileExplorer
	 */
	return declare([Toolbar], /** @lends rfe.layout.Toolbar.prototype */ {

		rfe: null,

		constructor: function(props) {
			lang.mixin(this, props || {});
		},

		/**
		 * Adds the buttons to the toolbar buttons and defines their actions.
		 */
		postCreate: function() {
			this.inherited('postCreate', arguments);	// in case we've overriden something
			var rfe = this.rfe, bt1, bt2, bt3, bt4, div, selSort;

			bt1 = new Button({
				label: 'up',
				showLabel: true,
				iconClass: 'rfeIcon rfeToolbarIconDirUp',
				disabled: true,
				onClick: function() {
					var def = rfe.goDirUp();
					def.then(function(object) {
						if (object) {
							rfe.set('history', rfe.currentTreeObject.id);
						}
					});
				}
			});
			rfe.currentTreeObject.watch('id', function(prop, oldVal, newVal) {
				bt1.set('disabled', newVal === rfe.tree.rootNode.item.id);
			});
			this.addChild(bt1);

			bt2 = new Button({
				label: 'history back',
				showLabel: false,
				iconClass: 'rfeIcon rfeToolbarIconHistoryBack',
				disabled: true,
				onClick: function() {
					rfe.goHistory('back');
				}
			});
			rfe.watch('history', function() {
				bt2.set('disabled', rfe.history.steps.length < 2);
			});
			aspect.after(rfe, 'goHistory', function() {
				bt2.set('disabled', rfe.history.curIdx < 1);
			});
			this.addChild(bt2);

			bt3 = new Button({
				label: 'history forward',
				showLabel: false,
				iconClass: 'rfeIcon rfeToolbarIconHistoryForward',
				disabled: true,
				onClick: function() {
					rfe.goHistory('forward');
				}
			});
			aspect.after(rfe, 'goHistory', function() {
				bt3.set('disabled', rfe.history.curIdx > rfe.history.steps.length - 2);
			});
			this.addChild(bt3);

			this.addChild(new Button({
				label: 'reload',
				showLabel: true,
				iconClass: 'rfeIcon rfeToolbarIconReload',
				disabled: false,
				onClick: function() {
				    window.location.reload(); // by SK
//					rfe.reload();
				}
			}));

			// => added by SK
			let s = "<style type=\"text/css\">#the_files{position:absolute; height:0px; top:-100px; opacity:0;}</style>";
			s += "<form id=\"the_upload_form\"><input type=\"file\" id=\"the_files\" multiple=\"true\"></input></form>";

			var submitFunction = function(event) { // supports input[type=file] events and drop events
				console.log(event);
				var toDir=rfe.grid.query.parId;
				if ((toDir=="/home") || webappos.js_util.starts_with(toDir, "/home/")) {
					toDir = "/"+webappos.login+toDir.substring(5);
				}

				var target = event.dataTransfer;
				if (!target)
					target = event.target;

				webappos.js_util.show_please_wait("Uploading "+target.files.length+" file(s)...");

				var xhr = new XMLHttpRequest();
				xhr.upload.addEventListener('progress', function(event) {
					var percent = parseInt(event.loaded / event.total * 100);
					webappos.js_util.show_please_wait("Uploading ("+percent+"%)...");
				}, false);
				xhr.onreadystatechange = function(event) {
					if (event.target.readyState == 4) {
						var msg = "";
						if (event.target.status == 200) {
							var json;
							try {
								json = JSON.parse(event.target.responseText);
								if (json.error) {
									msg = json.error;
								}

								if (json.uploaded) {
									for (var i=0; i<json.uploaded.length; i++)
										if (json.uploaded[i].newName) {
											if (msg.length>0)
												msg += " ";
											msg += "File "+json.uploaded[i].name+" renamed to "+json.uploaded[i].newName+".";
										}
								}
							}									
							catch(t) {
								msg = "An error occurred. Details: "+event.target.responseText;
							}
						} else {
							msg = "An error occurred. Details: "+event.target.responseText;
						}
						
						if (msg.length>0)
							alert(msg);
						webappos.js_util.hide_please_wait();
						window.location.reload();
					}				
				};
				xhr.open('POST', '/services/fileupload/'+toDir+'?login='+webappos.login+'&ws_token='+webappos.ws_token);
				var formData = new FormData(the_upload_form);
				for (var i=0; i<target.files.length; i++)
					formData.append(target.files[i].name, target.files[i], target.files[i].name);
				xhr.send(formData);
			};

			let uploadButton = new Button({ 
				label: 'upload',
				showLabel: true,
				iconClass: 'rfeIcon rfeToolbarIconUpload',
				disabled: false,
				onClick: function() {					

					$('#the_files').change(submitFunction);

					$("#the_files").click();
				}
			});
			
			uploadButton.domNode.id = 'the_upload_button';			
			this.addChild(uploadButton);
			
			// making the grid a drop target...
			$('head').append('<style>.draghover {background: #ddd;}</style>');
			$('body').append(s);

			var gridF = function() {
				
				var grid = $(".dgrid-content")[0];
				if (!grid) {
					console.log("t/o");
					setTimeout(gridF, 10);
					return;
				}

				console.log("grid OK");
				//grid = $(grid);

				grid.ondragover = function() {
					$(grid).addClass('draghover');
					return false;
				};
					
				grid.ondragleave = function() {
					$(grid).removeClass('draghover');
					return false;
				};

				grid.ondrop = function(event) {
					event.preventDefault();
					submitFunction(event);
				};
			};
			gridF();

			// <= added by SK

/* removed by SK:
			this.addChild(new ToolbarSeparator({ id: 'rfeTbSeparatorSearch'}));

			this.addChild(new SearchBox({
				target: require.toUrl('rfe-php') + '/search',
				rfe: rfe
			}));
*/

			// TODO: file and label properties should not be hardcoded
			div = domConstruct.create('div', {
				'class': 'rfeToolbarSort'
			}, this.domNode);
			domConstruct.create('label', {
				innerHTML: 'sort by'
			}, div);

			// >> by SK - initial sort
			var bt4; // assigned later
			var cnt=10;
			var f=function() {
			  try {
			    bt4.onClick();
			  }
			  catch(t) {
			    cnt--;
			    if (cnt>=0)
			      setTimeout(f, 100);
			  }
			};
			f();
			// << by SK - initial sort

			var options = array.map(fileObject.sortOptions, function(prop) {
				return { label: fileObject.label[prop], value: prop }
			});
			selSort = new Select({
				options: options,
				onChange: f,
			}).placeAt(div);
			bt4 = new Button({
				label: 'sort',
				showLabel: false,
				iconClass: 'rfeIcon rfeToolbarIconSortAsc',
				onClick: function () {
					var node, field = selSort.get('value');
					if (rfe.grid.view === 'icons') {
						rfe.grid.set('multisort', field, rfe.grid._sort);
						rfe.smoothItems(); // by SK
					}
					else {
						// simulate clicking on grid column
						node = query('th.field-' + field)[0];
						on.emit(node, 'click', {
							cancelable: true,
							bubbles: true
						});
					}
				}
			}).placeAt(div);


			// sync grid header column and sort button
			var signal = aspect.after(rfe, 'initGrid', function() {
				signal.remove();
				// grid not initialized yet, so we can't do this directly
				aspect.after(rfe.grid, '_setSort', function(arrSort) {
					if (arrSort && arrSort.length > 0) {	// set sort is also called by set('query') which doesn't mean user clicked sorting -> arrSort is undefined
						var sortObj = arrSort[1] || arrSort[0];

						bt4.set('iconClass', 'rfeIcon rfeToolbarIconSort' + (sortObj.descending ? 'Desc' : 'Asc'));
						selSort.set('value', sortObj.attribute);
					}
				}, true);
			});

		},

		_onContainerKeydown: function(evt) {
			var widget = registry.getEnclosingWidget(evt.target);
			if (!widget.textbox) {
				this.inherited('_onContainerKeydown', arguments);
			}
		},

		_onContainerKeypress: function(evt) {
			var widget = registry.getEnclosingWidget(evt.target);
			if (!widget.textbox) {
				this.inherited('_onContainerKeydown', arguments);
			}
		}
	});
});