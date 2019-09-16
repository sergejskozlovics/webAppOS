module(..., package.seeall)

require("initialize")

local empty_project_form_id = "owlgred_empty_project_dialog"

local button_height = 60

function open()
        lQuery("Counter"):attr("count", lQuery("Counter"):attr("count")+1)

	local form = lQuery.create("D#Form", {
		caption = "Sample Counter",
		id = empty_project_form_id,
		buttonClickOnClose = false,
--		eventHandler = d_handler("Close", "lua", "empty_project_dialog.close_dialog"),
		minimumHeight = 300,
		minimumWidth = 300,
	    component = {
	      lQuery.create("D#VerticalBox", {
	        component = {
	          lQuery.create("D#Label", {
		          caption = "Count = "..lQuery("Counter"):attr("count")
		        }),
		  lQuery.create("D#Button", {
		          caption = "Close"
		          ,minimumHeight = button_height
		          ,onClickEvent = "onCloseDialog"
		        }),
	        }
      	  })
    	}
  	})
	execute_d_command(form, "ShowModal")
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

