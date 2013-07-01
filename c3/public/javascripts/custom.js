$(document).ready(function() {

// date picker
$('.date').each(function() {
        $(this).datepicker({ dateFormat: 'yy-mm-dd' });
    });

// smooth scroll    
$('a[href^="#"]').bind('click.smoothscroll',function (e) {
    e.preventDefault();
    var target = this.hash,
        $target = $(target);
    $('html, body').animate({
        scrollTop: $(target).offset().top - 70
    }, 1000, 'swing', function () {
        window.location.hash = target;
    });
});    
    
    
});
