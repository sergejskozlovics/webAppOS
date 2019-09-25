
function log(...)
  local str = ""
  for _, v in ipairs(arg) do
    str = str .. tostring(v) .. "    "
  end
  console_log(str)
end

print = log

--tda = require("lua_tda")

lQuery = require("lQuery")

require ("lpeg")
