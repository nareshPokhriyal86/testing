/* 
SlashC jQuery Sliding Menu plugin
hi@slashc.com, www.slashc.com
*/
(function($)
{	
	/* plugin methods */
	var methods =
	{
		/* intialization */
		init : function(liId)
		{
			return this.each(function()
			{
				var mnu = $(this);	// the menu			
				if(!mnu.data('data'))
				{
					// header
					var h1 = mnu.children('h1').first(),
					h1d = 
					{
						span: h1.children('span').first(),
						home: h1.children('a.slashc-sliding-menu-home').first()
					};
					h1d.text = h1d.span.html();
					h1d.home.bind('click.slashcSlidingMenu', $.proxy(function(e)
					{
						methods.goHome.call(this);
						e.preventDefault();
					}, mnu));
					h1.data('d', h1d);
					
					// footer
					var p = mnu.children('p').first(),
					pd = 
					{
						span: p.children('span').first(),
						back: p.children('a.slashc-sliding-menu-back').first()
					};
					pd.text = pd.span.html();
					pd.back.bind('click.slashcSlidingMenu', $.proxy(function(e)
					{
						methods.goBack.call(this);
						e.preventDefault();
					}, mnu));
					p.data('d', pd);
					
					// list					
					var ul = mnu.children('ul').first(), li;
					mnu.data('lx', -ul.width()).data('lh', ul.height());				
					methods._iUl(ul, mnu); // init main list
					ul.css('height', mnu.data('lh')); // set the highest height
					mnu.removeData('lx').removeData('lh');					
					
					var md = { h1: h1, p: p, ul: ul, lx: 0, ttl: h1d.text }, mbc;
					ul.find('ul').css('display', 'none');
					// init position
					if (liId)
					{
						var ili = ul.find('li#' + liId);
						if (ili.length > 0)
						{
							var lid = ili.data('d'), el, eld;
							if (lid)
							{
								lid.cul.css('display', 'block');
								h1d.span.html(lid.txt);
								ul.css('left', lid.lx);
								md.lx = lid.lx; md.ttl = lid.txt;
								mbc = [];
								ili.parentsUntil('.slashc-sliding-menu').each(function()
								{
									el = $(this);
									if (el.is('li'))
									{
										eld = el.data('d');
										eld.cul.css('display', 'block');
										mbc.unshift({ lx: eld.lx, ttl: eld.txt });
									}
								});
								mbc.unshift({ lx: 0, ttl: h1d.text });
							}
						}
					}
					
					// menu
					mnu.data('d', md); // current list x
					mnu.data('bc', mbc || [{ lx: 0, ttl: h1d.text }]); // bread crumbs data
				}
			});
		},
		goBack : function()
		{
			methods._sUl(this, 'back'); // slide list back			
		},
		goHome : function()
		{
			methods._sUl(this, 'home'); // slide list home
		},
		// init ul
		_iUl : function(ul, mnu)
		{
			var lx = mnu.data('lx'), lh = mnu.data('lh');
			ul.children('li').has('ul').each(function()
			{
				var li = $(this), a = li.children('a').first();
				cul = li.children('ul').first(); // child list
				if (cul.height() > lh) mnu.data('lh', cul.height()); // check for max height
				cul.css('left', li.width()).css('top', -li.position().top); // reposition child list
				a.bind('click.slashcSlidingMenu', $.proxy(function(e) // on item click
				{
					var d = this.data('d');
					if (d)
					{
						methods._rUl(d.pul); // hide all other opened lists
						d.cul.css('display', 'block'); // show child list
						methods._sUl(d.mnu, d.lx, d.txt); // slide to show child list
					}
					e.preventDefault();
					// list x, child list, parent list, menu reference, header text
				}, li));
				li.data('d', { lx: lx, cul: cul, pul: ul, mnu: mnu, txt: li.children('a').children('span').html() });				
				mnu.data('lx', lx - cul.width()); // update position
				methods._iUl(cul, mnu); // further child list init
			});
		},
		// slide ul
		_sUl : function(mnu, lx, ttl)
		{
			// menu data, menu bread crumbs data, bc save
			var md = mnu.data('d'), mbc = mnu.data('bc'), bcs = true;
			// check if not back or home actions
			if (lx == 'back' || lx == 'home') 
			{
				var inf;
				if (lx == 'back') inf = mbc.pop(); // if back, get latest bread crumb
				else { inf = mbc[0]; if (inf) mbc.length = 1; } // if home, get first bread crumb and delete others
				if (inf)
				{
					lx = inf.lx;
					ttl = inf.ttl;
				}
				bcs = false;
			}
			if (ttl)
			{
				if (lx > 0) lx = 0;
				if (md.lx != lx)
				{
					var h1d = md.h1.data('d');
					if (bcs) mbc.push({ lx: md.lx, ttl: md.ttl }); // save bread crumb
					md.lx = lx; // cache new x position
					md.ul.stop().animate({ left: lx }, 300, 'linear'); // slide animation
					md.ttl = ttl; // store header text
					h1d.span.stop().fadeTo(150, 0, $.proxy(function()
					{
						this.h1.data('d').span.html(this.ttl); // update header text
					}, md)).fadeTo(150, 1);
					var pth = [], i = mbc.length;
					while(i--) { pth[i] = mbc[i].ttl; }
					pth.push(ttl);
					mnu.trigger('menuSlide', [pth]);
				}
			}
		},
		// reset ul, hides all child li ul
		_rUl : function(ul)
		{
			ul.children('li').children('ul').css('display', 'none'); // hide child lists
		}
	};
	
	/* Sliding Menu plugin */
	$.fn.slashcSlidingMenu = function(p1, p2)
	{
		/* p1 - method name, p2 - method options */
		/* p1 - options for init method, p2 - undefined */
		if (typeof p2 === 'undefined')
		{
			if (typeof p1 === 'undefined') return methods.init.call(this);
			else if (methods[p1]) return methods[p1].call(this);
			else return methods.init.call(this, p1);			
		}
		else if (methods[p1])
		{
			if (p2) return methods[p1].call(this, p2);
			else return methods[p1].call(this);
		}
		return this;
	};
	
	/* Attach plugin */
	$(document).ready(function()
	{
		$('div.slashc-sliding-menu').slashcSlidingMenu();
	});
	
})(jQuery);