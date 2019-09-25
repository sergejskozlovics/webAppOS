module(..., package.seeall)

-- redirects the print function to webAppOS console and
-- provides the lQuery variable for accessing webAppOS web memory
require("webappos")

local sample_project_form_id = "sample_project_dialog"

function open()
        lQuery("Counter"):attr("count", lQuery("Counter"):attr("count")+1)
	local form = lQuery.create("D#Form", {
	    caption = "Sample Counter",
	    id = sample_project_form_id,
	    buttonClickOnClose = false,
	    component = {
	      lQuery.create("D#VerticalBox", {
	        component = {
	          lQuery.create("D#Label", {
		          caption = "Count = "..lQuery("Counter"):attr("count")
		        }),
		  lQuery.create("D#Button", {
		          caption = "Close"
		          ,onClickEvent = "onCloseDialog"
		        }),
	        }
      	  })
    	}
  	})
	execute_d_command(form, "ShowModal")
end

function close_dialog()
	local form = lQuery("D#Form[id = " .. sample_project_form_id .. "]")
	execute_d_command(form, "Close")	
end

function execute_d_command(component, info)
	local cmd = lQuery.create("D#Command", {
	  receiver = component,
	  info = info
        })
        cmd:link("submitter", lQuery("TDAKernel::Submitter"))
end
