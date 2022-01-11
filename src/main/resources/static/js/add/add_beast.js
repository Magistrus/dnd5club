$('#beast_size').change(function () {
	switch($('#beast_size').val()){
	case 'TINY':
		break;
		$('#hp_dice_text').text('к4');
		$('#hp_dice').val('d4');
	case 'SMALL':
		$('#hp_dice_text').text('к6');
		$('#hp_dice').val('d4');
		break;
	case 'MEDIUM':
		$('#hp_dice_text').text('к8');
		$('#hp_dice').val('d8');
		break;
	case 'LARGE':
		$('#hp_dice_text').text('к10');
		$('#hp_dice').val('d10');
		break;
	case 'HUGE':
		$('#hp_dice_text').text('к12');
		$('#hp_dice').val('d12');
		break;
	case 'GARGANTUAN':
		$('#hp_dice_text').text('к20');
		$('#hp_dice').val('d20');
		break;
	}
});
$('#ability_con').change(function () {
	hp_bonus = getBonus($('#ability_con').val()) * $('#hp_dice_count').val();
	$('#hp_bonus').val(hp_bonus);
});
$('#hp_dice_count').change(function () {
	hp_bonus = getBonus($('#ability_con').val()) * $('#hp_dice_count').val();
	$('#hp_bonus').val(hp_bonus);
});
function getBonus(value) {
	if (value == 1)
		return -5;
	else if (value <= 3)
		return -4;
	else if (value <= 5)
		return -3;
	else if (value <= 7)
		return -2;
	else if (value <= 9)
		return -1;
	else if (value <= 11)
		return 0;
	else if (value <= 13)
		return 1;
	else if (value <= 15)
		return 2;
	else if (value <= 17)
		return 3;
	else if (value <= 19)
		return 4;
	else if (value <= 21)
		return 5;
	else if (value <= 23)
		return 6;
	else if (value <= 25)
		return 7;
	else if (value <= 27)
		return 8;
	else if (value <= 29)
		return 9;
	return 10;
}