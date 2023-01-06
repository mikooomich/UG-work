# Approximate the roots of a function with the Newton-Raphson method


.data

prompt: .asciiz "\nEnter a start value: "
nl: .asciiz "\n"
seperate: .asciiz "=x  F(x)="


# coeffiecents for f(x)
coEFFb: .word 4
coEFFc: .word 1 # the c term of f(x)

# coeffiecents for f'(x)
divA: .word 3
divB: .word 8


.text


main:
	# prompt a request to console
	li $v0, 4
	la $a0, prompt
	syscall

	# read from console, store into place
	li $v0, 7
	syscall
	mov.d $f2, $f0 # x



	# initialize coefficients, grow from f30 downwards
	# f24 divb
	# f26 divA
	# f28 coeff c
	# f30 coeff b
	lw $t3, coEFFb # move to co processor, convert to floating point standard
	mtc1 $t3, $f30
	cvt.d.w $f30, $f30
	lw $t3, coEFFc
	mtc1 $t3, $f28
	cvt.d.w $f28, $f28


	lw $t3, divA
	mtc1 $t3, $f26
	cvt.d.w $f26, $f26
	lw $t3, divB
	mtc1 $t3, $f24
	cvt.d.w $f24, $f24










	# do 6 times precision
	li $t2 6

# I dislike calculus with a passion
diriatve:
	beqz $t2, die

	# $f18 is temp regester I use
	# f20 fx result
	# f22 f' result
	
	# get fx
	mul.d $f20, $f2, $f2 #x^2
	mul.d $f22, $f20, $f26 # steal x^2 for dirv, calculate 3x^2
	mul.d $f18, $f20, $f30 #4x^2 in f(x)

	mul.d $f20, $f20, $f2 # x^3
	sub.d $f20, $f20, $f18
	add.d $f20, $f20, $f28 # f(x) final value



	# get f'(x)	
	mul.d $f18, $f24, $f2 # 8x
	sub.d $f22, $f22, $f18 # f'(x) final value




	# print x
	li $v0, 3
	mov.d $f12, $f2
	syscall

	la $a0, seperate
	li $v0, 4
	syscall

	# print fx
	li $v0, 3
	mov.d $f12, $f20
	syscall

	# new line
	la $a0, nl
	li $v0, 4
	syscall

		
		
		
	#  x - f/f'
	div.d $f18, $f20, $f22
	sub.d $f2, $f2, $f18 # write new x value

	sub $t2, $t2, 1 # decrement counter

	la $a0, nl
	li $v0, 4
	syscall
	b diriatve


die:
	li $v0, 10
	syscall