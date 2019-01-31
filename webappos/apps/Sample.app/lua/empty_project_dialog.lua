module(..., package.seeall)

require("initialize")

local empty_project_form_id = "owlgred_empty_project_dialog"

local button_height = 60

function open()
	local form = lQuery.create("D#Form", {
		caption = "OWLGrEd",
		id = empty_project_form_id,
		buttonClickOnClose = false,
		eventHandler = d_handler("Close", "lua", "empty_project_dialog.close_dialog"),
		minimumHeight = 300,
		minimumWidth = 300,
	    component = {
	      lQuery.create("D#VerticalBox", {
	        component = {
	          lQuery.create("D#Button", {
		          caption = "Visualize Ontology"
		          ,eventHandler = d_handler("Click", "lua", "empty_project_dialog.open_ontology()")
		          ,minimumHeight = button_height
		        }),
		  lQuery.create("D#Button", {
		          caption = "Visualize Ontology Module"
		          ,eventHandler = d_handler("Click", "lua", "empty_project_dialog.open_ontology_module()")
		          ,minimumHeight = button_height
		        }),
		  lQuery.create("D#Button", {
		          caption = "Create Ontology"
		          ,eventHandler = d_handler("Click", "lua", "empty_project_dialog.create_ontology()")
		          ,minimumHeight = button_height
		        }),
		  lQuery.create("D#Button", {
		          caption = "Close"
		          ,eventHandler = d_handler("Click", "lua_engine", "lua.empty_project_dialog.close_dialog()")
		          ,minimumHeight = button_height
--		          ,onClickEvent = "lua:empty_project_dialog.close_dialog()"
		        }),
	        }
      	  })
    	}
  	})
	execute_d_command(form, "ShowModal")
end

function d_handler(event_name, dll_name, transformation)
  return lQuery.create("D#EventHandler", {
      eventName = event_name
      ,transformationName = dll_name
      ,procedureName = transformation
  })
end

function execute_d_command(component, info, attr_list)
	attr_list = attr_list or {}
	attr_list["receiver"] = component
	attr_list["info"] = info
	local cmd = lQuery.create("D#Command", attr_list)
	tda.ExecuteCommand(cmd:id())
end


function close_dialog()
	local form = lQuery("D#Form[id = " .. empty_project_form_id .. "]")
	execute_d_command(form, "Close")	
end

