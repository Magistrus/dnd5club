<script src="js/scripts.js"></script>
<script src="js/simplebar.min.js"></script>

<script>
	document.getElementById('test-1').onclick = function() {
		document.getElementById('container_classes').classList.toggle('block_information');
	}

	document.getElementById('btn_close').onclick = function() {
		document.getElementById('container_classes').classList.toggle('block_information');
	}
</script>

<script>
	function outputsize() {
		width.value = left_block.offsetWidth;
			if (width.value > 900) {
				document.getElementById('card_container').classList.toggle('width_card_50');
			}
	}
	outputsize()

	new ResizeObserver(outputsize).observe(left_block)
</script>

<script>
	function switchTheme() {
		var check = document
			.getElementById('theme_css')
			.classList[0] === 'light';

		var element = document.getElementById('theme_css');


		if (check) {
			element.href = 'css/dark.css';
			element.classList.remove('light')
			element.classList.add('dark');
		} else {
			element.href = 'css/light.css';
			element.classList.remove('dark')
			element.classList.add('light');
		}

	}
</script>

<script>

</script>