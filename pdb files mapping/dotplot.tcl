#!/usr/bin/wish -f

#
# file:		dotplot.tcl
# purpose:	read a file containing two columns of integers representing
#		sequence positions and draw a dotplot
#


#
# Check that a command line argument has been given
#

if {$argc != 1} {
	error "usage: dotplot.tcl filename"
}


#
# Find the dimensions of the dotplot
#

set max_x 0
set max_y 0
set f [open [lindex $argv 0] r]
while {[gets $f line] >= 0} {
	scan $line "%d %d" x y
	if {$x>$max_x} { set max_x $x }
	if {$y>$max_y} { set max_y $y }
}


#
# Draw a blank canvas, a "PostScript" button and an "Exit"button
#

canvas .c -width $max_x -height $max_y
button .b -text "Write PostScript to temp.ps" \
	-command ".c postscript -file temp.ps"
button .exit_button -text "Exit" -command "destroy ."
pack .c .b .exit_button


#
# Plot dots on the canvas
#

set f [open [lindex $argv 0] r]
while {[gets $f line] >= 0} {
	scan $line "%d %d" x y
	.c create line $x $y [expr $x+1] [expr $y+1]
}
close $f
