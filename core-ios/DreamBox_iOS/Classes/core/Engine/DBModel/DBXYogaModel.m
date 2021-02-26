//
//  DBYogaModel.m
//  DreamBox_iOS
//
//  Created by zhangchu on 2020/12/18.
//

#import "DBXYogaModel.h"
#import "NSDictionary+DBXExtends.h"
#import "DBXParser.h"

@implementation DBXYogaModel

+ (DBXYogaModel *)modelWithDict:(NSDictionary *)dict {
    DBXYogaModel *model = [DBXYogaModel new];

    model.marginTop = [dict db_objectForKey:@"marginTop"];
    model.marginBottom = [dict db_objectForKey:@"marginBottom"];
    model.marginLeft = [dict db_objectForKey:@"marginLeft"];
    model.marginRight = [dict db_objectForKey:@"marginRight"];
    model.marginStart = [dict db_objectForKey:@"marginStart"];
    model.marginEnd = [dict db_objectForKey:@"marginEnd"];
    model.marginHorizontal = [dict db_objectForKey:@"marginHorizontal"];
    model.marginVertical = [dict db_objectForKey:@"marginVertical"];
    model.margin = [dict db_objectForKey:@"margin"];
    
    model.paddingTop = [dict db_objectForKey:@"paddingTop"];
    model.paddingLeft =  [dict db_objectForKey:@"paddingLeft"];
    model.paddingRight = [dict db_objectForKey:@"paddingRight"];
    model.paddingBottom = [dict db_objectForKey:@"paddingBottom"];
    model.paddingStart = [dict db_objectForKey:@"paddingStart"];
    model.paddingEnd = [dict db_objectForKey:@"paddingEnd"];
    model.paddingHorizontal = [dict db_objectForKey:@"paddingHorizontal"];
    model.paddingVertical = [dict db_objectForKey:@"paddingVertical"];
    model.padding = [dict db_objectForKey:@"padding"];
    
    model.width = [dict db_objectForKey:@"width"];
    model.height = [dict db_objectForKey:@"height"];
    model.maxWidth = [dict db_objectForKey:@"maxWidth"];
    model.minWidth = [dict db_objectForKey:@"minWidth"];
    model.maxHeight = [dict db_objectForKey:@"maxHeight"];
    model.minHeight = [dict db_objectForKey:@"minHeight"];
    
    model.isEnabled = [dict db_objectForKey:@"isEnabled"];
    model.flexDirection = [dict db_objectForKey:@"flex-direction"];
    model.justifyContent = [dict db_objectForKey:@"justify-content"];
    model.alignContent = [dict db_objectForKey:@"align-content"];
    model.alignItems = [dict db_objectForKey:@"align-items"];
    model.alignSelf = [dict db_objectForKey:@"align-self"];
    
    model.flexWrap = [dict db_objectForKey:@"flex-wrap"];
    model.overflow = [dict db_objectForKey:@"overflow"];
    model.display = [dict db_objectForKey:@"display"];
    model.flexGrow = [dict db_objectForKey:@"flex-grow"];
    model.flexShrink = [dict db_objectForKey:@"flexShrink"];
    model.flexBasis = [dict db_objectForKey:@"flex-basis"];
    
    model.positionType = [dict db_objectForKey:@"positionType"];
    model.positionLeft = [dict db_objectForKey:@"positionLeft"];
    model.positionTop = [dict db_objectForKey:@"positionTop"];
    model.positionRight = [dict db_objectForKey:@"positionRight"];
    model.positionBottom = [dict db_objectForKey:@"positionBottom"];
    model.start = [dict db_objectForKey:@"start"];
    model.end = [dict db_objectForKey:@"end"];
    model.aspectRatio = [dict db_objectForKey:@"aspectRatio"];
    
    return model;
}

@end
