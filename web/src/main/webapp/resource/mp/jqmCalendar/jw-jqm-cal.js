(function($) {
   $.jqmCalendar = function(element, options) {
	   //console.debug(element);
      var defaults = {
         // Array of events
         events : [],
         // Default properties for events
         begin : "begin",
         end : "end",
         summary : "summary",
         icon: "icon",
         url: "url",
         source : "source",
         // Sting to use when event is all day
         allDayTimeString: '',
         // Theme
         theme : "c",
         // Date variable to determine which month to show and which date to select
         date : new Date(),
         // Version
         version: "1.0.1",
         // Array of month strings (calendar header)
         months : ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"],
         // Array of day strings (calendar header)
         days : ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
         // Most months contain 5 weeks, some 6. Set this to six if you don't want the amount of rows to change when switching months.
         weeksInMonth : undefined,
         // Start the week at the day of your preference, 0 for sunday, 1 for monday, and so on.
         startOfWeek : 0,
         // List Item formatter, allows a callback to be passed to alter the contect of the list item
         listItemFormatter : listItemFormatter
      }

      var plugin = this,
          today = new Date();
      plugin.settings = null;

      var $element = $(element).addClass("jq-calendar-wrapper"),
          element = element,
          $table,
          $header,
          $tbody,
          $listview;

      function init() {
         plugin.settings = $.extend({}, defaults, options);
         plugin.settings.theme = $.mobile.getInheritedTheme($element, plugin.settings.theme);
         
         $table = $("<table/>");
         
         // Build the header
         var $thead = $("<thead/>").appendTo($table),
            $tr = $("<tr/>").appendTo($thead),
            $th = $("<th class='ui-bar-" + plugin.settings.theme + " header' colspan='7'/>");
         
         $previous = $("<a href='#' data-role='button' data-icon='arrow-l' data-iconpos='notext' class='previous-btn'>Previous</a>").click(function(event) {
        	 review(new Date(plugin.settings.date.getFullYear(), plugin.settings.date.getMonth() - 1, plugin.settings.date.getDate()));
         }).appendTo($th);
         
         $header = $("<span/>").appendTo($th);
         
         $previous = $("<a href='#' data-role='button' data-icon='arrow-r' data-iconpos='notext' class='next-btn'>Next</a>").click(function(event) {
        	 review(new Date(plugin.settings.date.getFullYear(), plugin.settings.date.getMonth() + 1, plugin.settings.date.getDate()));
            //$("#calendar").trigger('review',  new Date("2013-02-01"));	
         }).appendTo($th);
         
         $th.appendTo($tr);
         
         $tr = $("<tr/>").appendTo($thead);
         
         // The way of determing the labels for the days is a bit awkward, but works.
         for ( var i = 0, days = [].concat(plugin.settings.days, plugin.settings.days).splice(plugin.settings.startOfWeek, 7); i < 7; i++ ) {
            $tr.append("<th class='ui-bar-" + plugin.settings.theme + "'><span class='darker'>"  + days[i] + "</span></th>");
         }
         
         $tbody = $("<tbody/>").appendTo($table);
         
         $table.appendTo($element);
         $listview = $("<ul data-role='listview'/>").insertAfter($table);
         
         // Call refresh to fill the calendar with dates
         refresh(plugin.settings.date);      
      }
      
      function _firstDayOfMonth(date) {
         // [0-6] Sunday is 0, Monday is 1, and so on.
         return ( new Date(date.getFullYear(), date.getMonth(), 1) ).getDay();
      }
      
      function _daysBefore(date, fim) {
          // Returns [0-6], 0 when firstDayOfMonth is equal to startOfWeek, else the amount of days of the previous month included in the week.
         var firstDayInMonth = ( fim || _firstDayOfMonth(date) ),
             diff = firstDayInMonth - plugin.settings.startOfWeek;
         return ( diff > 0 ) ? diff : ( 7 + diff );
      }
      
      function _daysInMonth(date) {
         // [1-31]
         return ( new Date ( date.getFullYear(), date.getMonth() + 1, 0 )).getDate();
      }

      function _daysAfter(date, wim, dim, db) {
         // Returns [0-6] amount of days from the next month
         return    (( wim || _weeksInMonth(date) ) * 7 ) - ( dim || _daysInMonth(date) ) - ( db || _daysBefore(date));
      }
            
      function _weeksInMonth(date, dim, db) {
         // Returns [5-6];
         return ( plugin.settings.weeksInMonth ) ? plugin.settings.weeksInMonth : Math.ceil( ( ( dim || _daysInMonth(date) ) + ( db || _daysBefore(date)) ) / 7 );
      }
      
      function addCell($row, date, darker, selected) {
         var $td = $("<td class='ui-body-" + plugin.settings.theme + "'/>").appendTo($row),
             $a = $("<a href='#' class='ui-btn ui-btn-up-" + plugin.settings.theme + "'/>")
                  .html(date.getDate().toString())
                  .data('date', date)
                  .click(cellClickHandler)
                  .appendTo($td).on("taphold",function(){
          		    //alert($(this).html()+date);
                	  cellTapholdHandler($(this),date);
          		});

         if ( selected ) $a.click();
         
         if ( darker ) {
             $td.addClass("darker");
         }
         
         var importance = 0;
            
         // Find events for this date
         for ( var i = 0,
                   event,
                   begin = new Date(date.getFullYear(), date.getMonth(), date.getDate(), 0, 0, 0, 0),
                   end = new Date(date.getFullYear(), date.getMonth(), date.getDate() + 1, 0, 0, 0, 0);
               event = plugin.settings.events[i]; i++ ) {
            if ( event[plugin.settings.end] >= begin && event[plugin.settings.begin] <= end ) {
               importance++;
               if ( importance > 1 ) break;
            }
         }
            
         if ( importance > 0 ) {
             $a.append("<span>&bull;</span>");
         }
         
         if ( date.getFullYear() === today.getFullYear() &&
        	  date.getMonth() === today.getMonth() &&
        	  date.getDate() === today.getDate() ) {
        	 $a.addClass("ui-btn-today");
         } else {
        	 $a.addClass("importance-" + importance.toString());
         }
      }
      
      function cellTapholdHandler(a,date){
    	  //cellTapholdHandler
    	  
    	  location.replace($("#back_to_top").attr("href") + "?perDateTime=" + date.Format("yyyy-MM-dd hh:mm"));
      }
      function cellClickHandler(event) {
         var $this = $(this),
            date = $this.data('date');
         $tbody.find("a.ui-btn-active").removeClass("ui-btn-active");
         $this.addClass("ui-btn-active");
         
         if ( date.getMonth() !== plugin.settings.date.getMonth() ) {
            // Go to previous/next month
            refresh(date);
         } else {
            // Select new date
            $element.trigger('change', date);
         }
      }
      
      function refresh(date) {
         plugin.settings.date = date = date ||  plugin.settings.date || new Date();
                  
         var year = date.getFullYear(),
            month = date.getMonth(),
            daysBefore = _daysBefore(date),
            daysInMonth = _daysInMonth(date),
            weeksInMonth = plugin.settings.weeksInMonth || _weeksInMonth(date, daysInMonth, daysBefore);

         if (((daysInMonth + daysBefore) / 7 ) - weeksInMonth === 0)
             weeksInMonth++;
         
         // Empty the table body, we start all over...
         $tbody.empty();
         // Change the header to match the current month
         $header.html( plugin.settings.months[month] + " " + year.toString() );
      
         for (    var   weekIndex = 0,
                  daysInMonthCount = 1,
                  daysAfterCount = 1; weekIndex < weeksInMonth; weekIndex++ ) {
                     
            var daysInWeekCount = 0,
               row = $("<tr/>").appendTo($tbody);
            
            // Previous month
            while ( daysBefore > 0 ) {
               addCell(row, new Date(year, month, 1 - daysBefore), true);
               daysBefore--;
               daysInWeekCount++;
            }
            
            // Current month
            while ( daysInWeekCount < 7 && daysInMonthCount <= daysInMonth ) {
               addCell(row, new Date(year, month, daysInMonthCount), false, daysInMonthCount === date.getDate() );
               daysInWeekCount++;
               daysInMonthCount++;
            }
            
            // Next month
            while ( daysInMonthCount > daysInMonth && daysInWeekCount < 7 ) {
               addCell(row, new Date(year, month, daysInMonth + daysAfterCount), true);
               daysInWeekCount++;
               daysAfterCount++;
            }
         }
         
         $element.trigger('create');
      }

      $element.bind('change', function(event, begin) {
         var end = new Date(begin.getFullYear(), begin.getMonth(), begin.getDate() + 1, 0,0,0,0);
         // Empty the list
         $listview.empty();

         // Find events for this date
         for ( var i = 0, event; event = plugin.settings.events[i]; i++ ) {
            if ( event[plugin.settings.end] >= begin && event[plugin.settings.begin] < end ) {
               // Append matches to list
               var summary    = event[plugin.settings.summary],
                   beginTime  = (( event[plugin.settings.begin] > begin ) ? event[plugin.settings.begin] : begin ).toTimeString().substr(0,5),
                   endTime    = (( event[plugin.settings.end] < end ) ? event[plugin.settings.end] : end ).toTimeString().substr(0,5),
//                   timeString = beginTime + "-" + endTime,
                   timeString = beginTime,
                   $listItem  = $("<li></li>").appendTo($listview);
                   
               plugin.settings.listItemFormatter( $listItem, timeString, summary, event );
               
            }
         }
         
         $listview.trigger('create').filter(".ui-listview").listview('refresh');
      });
      
      function listItemFormatter($listItem, timeString, summary, event) {
//    	  var text =summary;
    	  var text = ( ( timeString != "00:00" ) ? timeString : plugin.settings.allDayTimeString ) + " " + summary;
         if (event[plugin.settings.icon]) {
            $listItem.attr('data-icon', event.icon);
         }
         if (event[plugin.settings.url]) {
            $('<a></a>').text( text ).attr('data-ajax','false').attr( 'href', event[plugin.settings.url] ).appendTo($listItem);
         } else {
            $listItem.text( text );
         }
         
      }
      
      function review(myDate){
    	  if(myDate.getMonth() <9){
				var month = "0"+(myDate.getMonth()+1);
			}else{
				var month = myDate.getMonth()+1;
			}
			firstDate = myDate.getFullYear()+"-"+month+"-01"
			$.mobile.loading( "show", {
	            text: "正在努力为您加载...",
	            textVisible: true,
	            theme: "b",
	            textonly: false,
	            html: ""
		    });
	    	$.ajax({
	  			type 	: "POST",
	  			url 	: plugin.settings.source,
	  			data:{"month":firstDate},
	  			dataType : 'json',
	  			contentType: "application/x-www-form-urlencoded; charset=utf-8",  
	  			cache : false,
	  			async:true,
	  			success:function(data){
	  				hide();
	  				var newEvents = [];
	  				$(data.data.dataRows).each(function(i,v){
	  					var dateObj =new Date(v.begin['time']);
	  					var endObj =new Date(v.end['time']);
	  					var cal={
	  							"summary" :v.summary,
	  							"begin" :dateObj,
	  							"end" :endObj,
	  							"url":v.url
	  					}
	  					newEvents.push(cal);
	  				});
	  				plugin.settings.events = newEvents;
	  				refresh(myDate);
	  			}
	  		});
      }
      $element.bind('refresh', function(event, date) {
         refresh(date);
      });

      $element.bind('review', function(event,myDate) {
          //alert(events.length);
    	  //alert(plugin.settings.source)
    	  //var newEvents1 = [ { "summary" : "Test event", "begin" : new Date("2013-02-05 00:00:00"), "end" : new Date("2013-02-07 00:00:00") }, { "summary" : "Test event", "begin" : new Date(), "end" : new Date() }, { "summary" : "Test event", "begin" : new Date(), "end" : new Date() } ];
    	  //ajax load
    	  review(myDate);
      });
      
      init();
   }

   $.fn.jqmCalendar = function(options) {
      return this.each(function() {
         if (!$(this).data('jqmCalendar')) {
             $(this).data('jqmCalendar', new $.jqmCalendar(this, options));
         }
      });
   }

})(jQuery);
