if {$argc > 1} {
  set project_file [lindex $argv 0]
  set run_id [lindex $argv 1]
} else {
  puts "usage: vivado -mode batch -source vivado_call.tcl -tclargs project.xpr run_id"
  exit
}

open_project $project_file
launch_runs $run_id
wait_on_run $run_id
