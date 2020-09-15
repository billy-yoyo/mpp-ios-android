//
//  JourneyViewController.swift
//  KotlinIOS
//
//  Created by Will Page on 14/09/2020.
//  Copyright © 2020 Evgeny Petrenko. All rights reserved.
//

import UIKit
import SharedCode

class JourneyViewController: UIViewController {
    
    private var presenter: JourneyInfoContractPresenter = JourneyInfoPresenter()
    private var journey: JourneyInfo? = nil
    private var tickets: [TicketInfo] = []

    override func viewDidLoad() {
        super.viewDidLoad()
        
        presenter.onViewTaken(view: self)

        // Do any additional setup after loading the view.
    }
    

    /*
    // MARK: - Navigation

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        // Get the new view controller using segue.destination.
        // Pass the selected object to the new view controller.
    }
    */

}

extension JourneyViewController : JourneyInfoContractView {
    
}
