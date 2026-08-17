function integrateddynamics_gallery:clear
fill 192 99 204 244 99 220 smooth_stone

# Stable cable topology: isolated, line, corner and tee.
setblock 196 100 208 integrateddynamics:cable
fill 200 100 208 203 100 208 integrateddynamics:cable
setblock 208 100 208 integrateddynamics:cable
setblock 209 100 208 integrateddynamics:cable
setblock 209 100 209 integrateddynamics:cable
setblock 214 100 208 integrateddynamics:cable
setblock 213 100 208 integrateddynamics:cable
setblock 215 100 208 integrateddynamics:cable
setblock 214 101 208 integrateddynamics:cable

# Core parts on four faces, normalized by the add-on to neutral models.
setblock 220 100 208 integrateddynamics:cable
data merge block 220 100 208 {realCable:1b,partContainer:{parts:[{__partType:"integrateddynamics:block_reader",__side:"south"},{__partType:"integrateddynamics:inventory_writer",__side:"north"},{__partType:"integrateddynamics:static_light_panel",__side:"east"},{__partType:"integrateddynamics:connector_omni_directional",__side:"west"}]}}

# Extension-family parts: tunnels, terminals, crafting and scripting.
setblock 228 100 208 integrateddynamics:cable
data merge block 228 100 208 {realCable:1b,partContainer:{parts:[{__partType:"integratedtunnels:importer_item",__side:"south"},{__partType:"integratedterminals:terminal_storage",__side:"north"},{__partType:"integratedcrafting:interface_crafting",__side:"east"},{__partType:"integratedscripting:terminal_scripting",__side:"west"}]}}

# Part-only housing: no cable core.
setblock 236 100 208 integrateddynamics:cable
data merge block 236 100 208 {realCable:0b,partContainer:{parts:[{__partType:"integratedtunnels:player_simulator",__side:"up"},{__partType:"integratedcrafting:crafting_writer",__side:"south"}]}}

# Facades with cable and part apertures.
setblock 204 100 216 integrateddynamics:cable
data merge block 204 100 216 {realCable:1b,facadeBlockTag:{Name:"minecraft:stone"},partContainer:{parts:[]}}
setblock 212 100 216 integrateddynamics:cable
data merge block 212 100 216 {realCable:1b,facadeBlockTag:{Name:"minecraft:oak_planks"},partContainer:{parts:[{__partType:"integrateddynamics:block_reader",__side:"south"}]}}

# Stock controls.
setblock 220 100 216 minecraft:stone
setblock 224 100 216 minecraft:oak_planks

scoreboard players set #ready id_gallery 1
function integrateddynamics_gallery:verify
