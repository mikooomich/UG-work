#Convert an integer to binary


.data
prompt: .asciiz "Hewwo! Please enter a positive 32-bit integer: "
nl: .asciiz ""

.text

main:
# prompt a request to console, read from console
li $v0, 4
la $a0, prompt
syscall

li $v0, 5
syscall


#load value read into reg t1
la $t1, ($v0) 


# print sign bit zero
li $a0, 0
li $v0, 1
syscall


# for calculating the number to subtract
li $s3, 30 # the number of bits of number to process, the line below already loads the 31st bit 
li $t7, 1 # num to subtr
li $t6, 0 # count of times multiplied


# the following calculates the power to subtract, by multiplying over and over again
#ex 2^31, 2^30.... etc
# should have just calculated the highest power once and divided by 2, instead of calculating the power each time, apologies for the inefficiency
exponent:
	
	blt $s3, 0, exit # exit program when processed all powers of 2
	bge $t6, $s3, loop # break loop when we have the right power of 2
	
	# multiply and increment times multiplied
	mul $t7, $t7, 2
	add $t6, $t6, 1

	b exponent

	
	
loop:
	# move onto next power of 2, counting down
	# reset values used to calculate powers
	sub $s3, $s3, 1 
	li $t6, 0 

	# skip subtraction if the power of 2 is greater than the input number
	bge $t1, $t7, subtr
	blt $t1, $t7, next 

	beqz $t1, exit 


next:
	#print 0
	li $a0, 0
	li $v0, 1
	syscall
	li $t7, 1 # reset num to subtract
	
	b exponent



subtr:
	# print 1, subtract power from input value
	sub $t1, $t1, $t7
	li $a0, 1
	li $v0, 1
	syscall
	li $t7, 1 # reset num to subtract
	
	b exponent


exit:
	li $v0, 10
	syscall
