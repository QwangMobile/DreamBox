//
//  DBContainerViewFrame.m
//  DreamBox_iOS
//
//  Created by zhangchu on 2021/1/13.
//

#import "DBContainerViewFrame.h"
#import "DBFrameModel.h"
#import "DBDefines.h"
#import "DBRenderModel.h"
#import "DBDefines.h"
#import "UIView+Yoga.h"
#import "DBFrameLayout.h"

@implementation DBContainerViewFrame

+ (DBContainerView *)containerViewWithRenderModel:(DBRenderModel *)renderModel pathid:(NSString *)pathId delegate:(nonnull id<DBContainerViewDelegate>)delegate{
    DBContainerViewFrame *container = [DBContainerViewFrame new];
    container.containerDelegate = delegate;
    [container frameLayoutWithContainer:container renderModel:renderModel];
    container.pathTid = pathId;
    
    return container;
}

- (void)frameLayoutWithContainer:(UIView *)container renderModel:(DBRenderModel *)renderModel{
    [DBFrameLayout frameLayoutWithView:container withModel:renderModel.frameModel contentSize:CGSizeZero];
    
    container.backgroundColor = [UIColor orangeColor];
    CGSize contentSize = CGSizeMake([DBDefines db_getUnit:renderModel.frameModel.width], [DBDefines db_getUnit:renderModel.frameModel.height]);
    NSArray *renderArray = renderModel.children;
    for (int i = 0; i < renderArray.count ; i ++) {
        NSDictionary *dict = renderArray[i];
        NSString *_type = [dict objectForKey:@"_type"];
        if([_type isEqual:@"layout"]){
            DBRenderModel *subRenderModel = [DBRenderModel modelWithDict:dict];
            UIView *subContainer = [self.containerDelegate containerViewWithRenderModel:subRenderModel pathid:self.pathTid];
            [container addSubview: subContainer];
            [DBFrameLayout frameLayoutWithView:subContainer withModel:subRenderModel.frameModel contentSize:contentSize];
        }else {
            Class cls = [[DBFactory sharedInstance] getModelClassByType:_type];
            DBViewModel *viewModel = [cls modelWithDict:dict];
            UIView *view = [self modelToView:viewModel];
            [view configureLayoutWithBlock:^(YGLayout * _Nonnull layout) {
                layout.isEnabled = NO;
                layout.isIncludedInLayout = NO;
            }];
            //添加到模型数组,渲染数组中
            [self addToAllContainer:container item:view andModel:viewModel];
            [DBFrameLayout frameLayoutWithView:view withModel:viewModel.frameLayout contentSize:contentSize];
        }
    }
}

//- (void)setUpFrameModel:(DBFrameModel *)frameModel inView:(UIView *)view {
//    CGFloat x,y,w,h;
//    x = [DBDefines db_getUnit:frameModel.marginLeft];
//    y = [DBDefines db_getUnit:frameModel.marginTop];
//    w = [DBDefines db_getUnit:frameModel.width];
//    h = [DBDefines db_getUnit:frameModel.height];
//    view.frame = CGRectMake(x, y, w, h);
//}

@end