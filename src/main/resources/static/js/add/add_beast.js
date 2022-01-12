$('#beast_size').change(function () {
	switch($('#beast_size').val()){
	case 'TINY':
		$('#hp_dice_text').text('к4');
		$('#hp_dice').val('d4');
		break;
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
	changeHpFormula();
});
$('#ability_con').change(function () {
	let hp_bonus = getBonus($('#ability_con').val()) * $('#hp_dice_count').val();
	$('#hp_bonus').val(hp_bonus);
	changeHpFormula();
});
$('#hp_dice_count').change(function () {
	let hp_bonus = getBonus($('#ability_con').val()) * $('#hp_dice_count').val();
	$('#hp_bonus').val(hp_bonus);
	changeHpFormula();
});
$('#hp_dice').change(function () {
	changeHpFormula();
});
$('#hp_bonus').change(function () {
	changeHpFormula();
});
$('#ability_dex').change(function () {
	if($('#armor').val() == ''){
		let ac = 10 + getBonus($('#ability_dex').val());
		$('#ac').val(ac);
	} else {
		changeAC();
	}
});
$('#armor').change(function () {
	changeAC();
});
$('details').delegate('.add_trait', 'click', function() {
	var $item = $(this);
	addTrait($(this), 'trait');
});
$('details').delegate('.add_action', 'click', function() {
	var $item = $(this);
	addTrait($(this), 'action');
});
$('details').delegate('.add_bonus', 'click', function() {
	var $item = $(this);
	addTrait($(this), 'bonus');
});
$('details').delegate('.add_reaction', 'click', function() {
	var $item = $(this);
	addTrait($(this), 'reaction');
});
$('details').delegate('.add_legendary', 'click', function() {
	var $item = $(this);
	addTrait($(this), 'legendary');
});
function addTrait($item, name){
	$parent = $item.parents('.opportunities');
	newdiv = document.createElement( "div" ),
	$(newdiv).addClass('opportunities');
	$(newdiv).load('/profile/beast/' + name, function() {
		$parent.after(newdiv);
	});
}
function changeHpFormula(){
	let average = 1;
	let formula = '<span class="dice_text">';
	if($('#hp_dice_count').val() !=0){
		formula+= $('#hp_dice_count').val();
		average = parseInt($('#hp_dice_count').val(), 10);
	}
	formula+= $('#hp_dice_text').text()+'</span>';
	switch($('#hp_dice_text').text()){
		case 'к4':
			average*=2.5;
			break;
		case 'к6':
			average*=3.5;
			break;
		case 'к8':
			average*=4.5;
			break;
		case 'к10':
			average*=5.5;
			break;
		case 'к12':
			average*=6.5;
			break;
		case 'к20':
			average*=10.5;
		break;
	}
	if ($('#hp_bonus').val() != 0){
		formula+= ($('#hp_bonus').val()>0 ? ' + ' : ' - ') + Math.abs($('#hp_bonus').val());
		average+=parseInt($('#hp_bonus').val(), 10);
	}
	$('#hp_formula').html(Math.max(Math.floor(average), 1) + ' (' + formula + ')');
}
function changeAC(){
	switch($('#armor').val()){
	case 'LEATHER':
		$('#ac').val(11 + getBonus($('#ability_dex').val()));
		break;
	case 'RIVETED_LEATHER':
		$('#ac').val(12 + getBonus($('#ability_dex').val()));
		break;
	case 'HIDE':
		$('#ac').val(12 + Math.max(getBonus($('#ability_dex').val())), 2);
		break;
	case 'CHAINMAIL':
		$('#ac').val(13 + Math.max(getBonus($('#ability_dex').val())), 2);
		break;
	case 'SCALED':
	case 'BREASTPLATE':
	case 'CUIRASS':
		$('#ac').val(14 + Math.max(getBonus($('#ability_dex').val())), 2);
		break;
	case 'RING_MAIL':
		$('#ac').val(14);
		break;
	case 'HALF_PLATE':
		$('#ac').val(15);
		break;
	case 'CHAIN_MAIL':
		$('#ac').val(16);
		break;
	case 'PLATE':
		$('#ac').val(18);
		break;
	}
	if ($('#armor').val() == 'TEXT'){
		$('#ac_text').removeClass('hide_block');
	} else {
		$('#ac_text').addClass('hide_block');
	}
}
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